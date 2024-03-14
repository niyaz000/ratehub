package com.github.niyaz000.ratehub.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class TenantCreateRequest {

  @NotBlank
  @Size(max = 255)
  private String name;

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
