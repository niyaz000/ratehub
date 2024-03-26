package com.github.niyaz000.ratehub.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.github.niyaz000.ratehub.constants.ErrorConstants.INVALID_AUTH_CONFIG;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class TenantCreateRequest {

  public static final String AUTH_CONFIG = "auth_config";

  @NotBlank
  @Size(max = 255)
  private String name;

  public Map<String, String> getAuthConfig() {
    try {
      return (Map<String, String>) config.getOrDefault(AUTH_CONFIG, new HashMap<String, String>());
    } catch (Exception ex) {
      throw new ValidationException(INVALID_AUTH_CONFIG);
    }
  }

  private Map<String, Object> config = new HashMap<>();

  public void setName(String name) {
    this.name = name.trim();
  }

  @Override
  public String toString() {
    return "TenantCreateRequest{" +
            "name='" + name + '\'' +
            '}';
  }

}
