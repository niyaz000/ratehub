package com.github.niyaz000.ratehub.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class RatingGetResponse {
  private Long id;

  private String tenantName;

  private Double score;

  private String variation;

  private boolean verified;

  private String userId;

  private String productId;

  private Double weight;

  private String region;

  private Map<String, Object> meta = new HashMap<>();

  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone="UTC")
  private Date createdAt;

  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone="UTC")
  private Date updatedAt;


}
