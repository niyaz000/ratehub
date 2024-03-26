package com.github.niyaz000.ratehub.mapper;

import com.github.niyaz000.ratehub.model.Tenant;
import com.github.niyaz000.ratehub.request.TenantCreateRequest;
import com.github.niyaz000.ratehub.request.TenantUpdateRequest;
import com.github.niyaz000.ratehub.response.TenantCreateResponse;
import com.github.niyaz000.ratehub.response.TenantListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper
public interface TenantMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "updatedAt", source = "updatedAt")
  @Mapping(target = "config", source = "config")
  @Mapping(target = "deleted", source = "deleted")
  TenantCreateResponse mapTenantCreateRequestToResponse(Tenant tenant);

  @Mapping(target = "name", source = "name")
  @Mapping(target = "config", source = "config")
  @Mapping(target = "deleted", constant = "false")
  Tenant mapTenantCreateRequestToTenant(TenantCreateRequest request);

  default Tenant mapTenantUpdateRequestToTenant(TenantUpdateRequest request, Tenant tenant) {
    tenant.setConfig(request.getConfig());
    return tenant;
  }

  default TenantListResponse mapTenantsToTenantListResponse(Page<Tenant> tenants) {
    var response = new TenantListResponse();
    tenants.stream()
            .forEach(tenant -> response.addTenant(mapTenantCreateRequestToResponse(tenant)));
    return response;
  }
}
