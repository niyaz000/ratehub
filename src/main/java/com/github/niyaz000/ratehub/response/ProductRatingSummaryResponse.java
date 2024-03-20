package com.github.niyaz000.ratehub.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ProductRatingSummaryResponse {

  private String tenantName;

  private String productId;

  private long totalCount;

  private double score;

  private Date updatedAt;

  private List<StarRating> starRatings;

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @Data
  @AllArgsConstructor
  public static class StarRating {
    private int starValue;

    private int totalCount;

    private long percentage;

  }

}
