package com.github.niyaz000.ratehub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OverallRatingScheduler {

  @Scheduled(fixedRate = 60000)
  public void schedule() {

  }

}
