package com.github.niyaz000.ratehub.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "tenants")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@DynamicUpdate
@Builder
public class Tenant extends BaseEntity {

  @Column(name = "name", nullable = false, updatable = false)
  private String name;

  @Type(JsonType.class)
  @Column(name = "config", nullable = false, columnDefinition = "json")
  private Map<String, Object> config = new HashMap<>();

  @Column(name = "deleted", nullable = false)
  private boolean deleted;

}
