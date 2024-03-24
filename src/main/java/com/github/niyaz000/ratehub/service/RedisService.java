package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import com.github.niyaz000.ratehub.util.ObjectMapperUtil;
import com.github.niyaz000.ratehub.util.OverallScoreComputeService;
import com.github.niyaz000.ratehub.util.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.params.ZAddParams;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService {

  @Value("${app.max-concurrent-rating-computation-worker-count}")
  String maxWorkerCount;

  private ObjectMapperUtil mapperUtil;

  private static final String PRODUCT_RATINGS_CHANGED_SET_KEY = "modified_product_ratings_set";

  private static final String MUTEX_KEY = "scheduler_mutex";

  private static final Duration MUTEX_TTL = Duration.ofSeconds(30);

  private final RedisClient redisClient;

  private final OverallScoreComputeService overallScoreComputeService;

  public void getChangedProductRatings() {
    boolean acquiredLock = acquireLock();
    try {
      if (!acquiredLock) {
        log.info("unable to acquire lock, skipping scheduling");
        return;
      }
      var product = getProduct();
      scheduleJobsToRecomputeRatings(product);
    } finally {
      if (acquiredLock) {
        log.info("releasing acquired lock");
        releaseLock();
      }
    }
  }

  private void scheduleJobsToRecomputeRatings(List<String> changedRatings) {
    changedRatings
      .stream()
      .map(rating -> mapperUtil.fromJsonString(rating, RatingChangeDto.class))
      .forEach(overallScoreComputeService::computeNewScore);
  }

  private List<String> getProduct() {
    return redisClient.zRangeByScore(PRODUCT_RATINGS_CHANGED_SET_KEY, Double.NEGATIVE_INFINITY, Instant.now().minus(Duration.ofSeconds(10)).getNano());
  }

  private boolean acquireLock() {
    return redisClient.setNex(MUTEX_KEY, "0", MUTEX_TTL);
  }

  private void releaseLock() {
    redisClient.deleteKey(MUTEX_KEY);
  }

  public List<Object> enqueueEvent(String key, String body) {
    var score = Instant.now().getNano();
    log.info("publishing event with key {} body {} score {}", key, body, score);
    try(var txn = redisClient.getTransaction()) {
      txn.zadd(PRODUCT_RATINGS_CHANGED_SET_KEY, score, key, new ZAddParams().nx());
      txn.append(key, body);
      return txn.exec();
    }
  }

}
