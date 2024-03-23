package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.model.Rating;
import com.github.niyaz000.ratehub.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class RatingPublisherService {

  private static final String RATING_CHANGE_FOR_PRODUCT_KEY = "%s::%s";

  private final RedisService redisService;

  private final ObjectMapperUtil mapperUtil;

  public void publishEvent(String tenantName,
                           Rating rating) {
    log.info("processing rating change event with txtId {}", rating.getTxnId());
    String key = String.format(RATING_CHANGE_FOR_PRODUCT_KEY, tenantName, rating.getProductId());
    redisService.enqueueEvent(key, rating.getTxnId().toString());
  }

}
