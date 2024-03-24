package com.github.niyaz000.ratehub.request;

import com.github.niyaz000.ratehub.constants.Action;
import lombok.Data;

import java.util.Map;

@Data
public class RatingSummaryComputeRequest {

  private final Action action;

  private final long tenantId;

  private final double score;

  private Map<String, Object> meta;

  private final double totalWeight;

  private final double totalWeightedSum;

  private final int totalCount;

}
