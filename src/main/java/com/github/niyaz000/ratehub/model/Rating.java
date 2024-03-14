package com.github.niyaz000.ratehub.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Map;

@Entity
@Table(name = "ratings")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@DynamicUpdate
@Builder
public class Rating extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  @NotBlank
  private String productId;

  @Column(name = "user_id", nullable = false)
  @NotBlank
  private String userId;

  @Column(name = "tenant_id", nullable = false)
  @NotNull
  private Integer tenantId;

  @Type(JsonType.class)
  @Column(name = "meta", columnDefinition = "json")
  private Map<String, Object> meta;

  @Column(name = "score", columnDefinition = "FLOAT default 0")
  private Double score;

  @Column(name = "verified", columnDefinition = "BOOLEAN default false")
  private boolean verified;

  @Column(name = "weight", columnDefinition = "FLOAT default 0")
  private Double weight;

  @Column(name = "region")
  private String region;

  @Column(name = "variation", columnDefinition = "INTEGER default 0")
  private String variation;

  @Column(name = "flushed", columnDefinition = "BOOLEAN default false")
  private boolean flushed;

}
