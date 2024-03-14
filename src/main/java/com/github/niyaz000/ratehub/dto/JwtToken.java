package com.github.niyaz000.ratehub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JwtToken {

  @NotBlank
  private String tenantName;

  @NotBlank
  private String role;

  private String userId;

}
