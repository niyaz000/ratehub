package com.github.niyaz000.ratehub.util;

import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OverallScoreComputeService {

  @Async
  public void computeNewScore(RatingChangeDto ratingChangeDto) {

  }


}
