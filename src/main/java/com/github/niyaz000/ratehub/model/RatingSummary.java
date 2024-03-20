package com.github.niyaz000.ratehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ratings_summary")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@DynamicUpdate
@Builder
public class RatingSummary extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "five_star_count")
  private Integer fiveStarCount = 0;

  @Column(name = "four_star_count")
  private Integer fourStarCount = 0;

  @Column(name = "three_star_count")
  private Integer threeStarCount = 0;

  @Column(name = "two_star_count")
  private Integer twoStarCount = 0;

  @Column(name = "one_star_count")
  private Integer oneStarCount = 0;

  @Column(name = "total_weight", nullable = false)
  private Double totalWeight = 0.0;

  @Column(name = "total_weighted_sum", nullable = false)
  private Double totalWeightedSum = 0.0;

  @Column(name = "total_count", nullable = false)
  private Long totalCount = 0L;

  @Column(nullable = false)
  private Double score = 0.0;

  @Column(name = "product_id", nullable = false, updatable = false)
  private String productId;

  @Column(name = "tenant_id", nullable = false)
  private Long tenantId;

  public static RatingSummary defaultRatingSummary(Long tenantId,
                                                   String productId) {
    return RatingSummary
      .builder()
      .productId(productId)
      .tenantId(tenantId)
      .build();
  }

}
