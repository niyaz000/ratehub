package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dao.RatingSummaryDao;
import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import com.github.niyaz000.ratehub.dto.ScoreComputeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleAverageScoreRatingService implements RatingScoreCalculator {

  private final RatingSummaryDao ratingSummaryDao;

  @Override
  public ScoreComputeResult computeScore(RatingChangeDto changeDto) {
    log.info("processing overall rating score computation for product {}, value {}",
      changeDto.getTenantId(),
      changeDto);

      return handleNewRatingAddition(changeDto);
//
//    return ScoreComputeResult
//      .builder()
//      .totalWeightedSum(0)
//      .totalWeight(0)
//      .score(0)
//      .build();
  }

  private ScoreComputeResult handleNewRatingAddition(RatingChangeDto changeDto) {
//    var rating = changeDto.getNewValue();
//    var summary = ratingSummaryDao.findByTenantIdAndProductId(rating.getTenantId(), rating.getProductId()).orElseThrow();
//    double score = changeDto.getNewValue().getScore();
//    double newScore = summary.getTotalWeightedSum() / summary.getTotalWeight();
//    return ScoreComputeResult
//      .builder()
//      .totalWeightedSum(summary.getTotalWeightedSum() + score)
//      .totalWeight(summary.getTotalWeight() + 1)
//      .score(newScore)
//      .build();
    return ScoreComputeResult
      .builder()
      .totalWeightedSum(0)
      .totalWeight(0)
      .score(0)
      .build();
  }

}
