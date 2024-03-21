package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import org.springframework.stereotype.Component;

@Component
public class SimpleAverageScoreRatingService implements RatingScoreCalculator {

  @Override
  public int computeScore(RatingChangeDto changeDto) {
    return 0;
  }

}
