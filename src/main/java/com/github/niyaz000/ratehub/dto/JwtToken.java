package com.github.niyaz000.ratehub.dto;

import com.github.niyaz000.ratehub.constants.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class JwtToken {

  public JwtToken(String tenantName, String role, String userId) {
    this.tenantName = Objects.requireNonNull(tenantName, "tenant_name must not be null");
    this.role = Role.valueOf(Objects.requireNonNull(role, "role must not be null").toUpperCase());
    this.userId = userId;
  }

  private String tenantName;

  private Role role;

  private String userId;

}
