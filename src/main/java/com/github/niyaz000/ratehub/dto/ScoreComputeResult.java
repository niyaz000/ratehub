package com.github.niyaz000.ratehub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreComputeResult {

  double totalWeightedSum;

  double totalWeight;

  double score;

}
