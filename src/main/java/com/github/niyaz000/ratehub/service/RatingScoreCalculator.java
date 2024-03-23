package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import com.github.niyaz000.ratehub.dto.ScoreComputeResult;

public interface RatingScoreCalculator {

  ScoreComputeResult computeScore(RatingChangeDto changeDto);

}
