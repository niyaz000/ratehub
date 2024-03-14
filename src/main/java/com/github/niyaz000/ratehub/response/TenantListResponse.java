package com.github.niyaz000.ratehub.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class TenantListResponse {

  private final List<TenantCreateResponse> tenants = new ArrayList<>();

  public void addTenant(TenantCreateResponse tenant) {
    tenants.add(tenant);
  }

}
