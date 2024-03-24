package com.github.niyaz000.ratehub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "ratings_audit")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@DynamicUpdate
@Builder
public class RatingsAudit extends BaseEntity {

  @Column(name = "action", nullable = false)
  private String action;

  @Column(name = "rating_id")
  private Long ratingId;

  @Column(name = "product_id", nullable = false)
  private String productId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "tenant_id", nullable = false)
  private Integer tenantId;

  @Column(name = "meta", columnDefinition = "jsonb")
  private String meta;

  @Column(name = "score")
  private Float score;

  @Column(name = "verified")
  private Boolean verified;

  @Column(name = "weight")
  private Float weight;

  @Column(name = "variation", nullable = false)
  private String variation;

  @Column(name = "region", nullable = false)
  private String region;

  @Column(name = "version")
  private Long version;

  @Column(name = "flushed")
  private Boolean flushed;

  @Column(name = "txn_id", nullable = false, columnDefinition = "uuid")
  private UUID txnId;

  @Column(name = "user_name", nullable = false)
  private String userName;

}
