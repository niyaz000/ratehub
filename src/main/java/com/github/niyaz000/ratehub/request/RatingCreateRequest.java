package com.github.niyaz000.ratehub.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class RatingCreateRequest {

  @Min(1)
  @Max(5)
  private Double score;

  private boolean verified;

  @Min(0)
  @Max(1)
  private double weight;

  @Size(min = 1, max = 16)
  private String variation;

  @Size(min = 3, max = 3)
  private String region;

  private Map<String, Object> meta = new HashMap<>();

}
