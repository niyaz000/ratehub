package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dto.RatingChangeDto;

public interface RatingScoreCalculator {

  int computeScore(RatingChangeDto changeDto);

}
