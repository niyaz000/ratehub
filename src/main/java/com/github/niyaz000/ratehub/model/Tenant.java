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
import java.util.Objects;

import static io.opentelemetry.javaagent.shaded.io.opentelemetry.api.internal.ConfigUtil.defaultIfNull;

@Entity
@Table(name = "tenants")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@DynamicUpdate
@Builder
public class Tenant extends BaseEntity {

  public static final String JWT_CONFIG_KEY = "jwt_config";

  @Column(name = "name", nullable = false, updatable = false)
  private String name;

  @Type(JsonType.class)
  @Column(name = "config", nullable = false, columnDefinition = "json")
  private Map<String, Object> config = new HashMap<>();

  @Column(name = "deleted", nullable = false)
  private boolean deleted;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class JwtConfig {
    private String keyOne;

    private String TwoOne;
  }

  public Map<String, String> getSigningKeyConfig() {
    var value = (Map<String, String>) defaultIfNull(config.get(JWT_CONFIG_KEY), new HashMap<>());
    return Objects.requireNonNull(value);
  }

}
