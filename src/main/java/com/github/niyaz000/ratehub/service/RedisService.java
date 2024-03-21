package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import com.github.niyaz000.ratehub.util.ObjectMapperUtil;
import com.github.niyaz000.ratehub.util.OverallScoreComputeService;
import com.github.niyaz000.ratehub.util.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

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
      var changedRatings = getFirst10ChangedProductRatings(PRODUCT_RATINGS_CHANGED_SET_KEY);
      scheduleJobsToRecomputeRatings(changedRatings);
    } finally {
      if (acquiredLock) {
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

  private List<String> getFirst10ChangedProductRatings(String key) {
    return redisClient.zrange(key, 0, 10);
  }


  private boolean acquireLock() {
    return redisClient.setNex(MUTEX_KEY, "0", MUTEX_TTL);
  }

  private void releaseLock() {
    redisClient.deleteKey(MUTEX_KEY);
  }

}
