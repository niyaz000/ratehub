package com.github.niyaz000.ratehub.controller;

import com.github.niyaz000.ratehub.request.TenantCreateRequest;
import com.github.niyaz000.ratehub.request.TenantUpdateRequest;
import com.github.niyaz000.ratehub.response.TenantCreateResponse;
import com.github.niyaz000.ratehub.response.TenantListResponse;
import com.github.niyaz000.ratehub.service.TenantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.github.niyaz000.ratehub.constants.ApiConstants.*;
import static com.github.niyaz000.ratehub.constants.Routes.*;


@RestController
@Validated
@Slf4j
public class TenantController {

  private static final String NAME = "name";

  private final TenantService tenantService;

  public TenantController(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @PostMapping(TENANTS_ENDPOINT)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('SYSTEM')")
  public TenantCreateResponse create(@Valid @RequestBody TenantCreateRequest request) {
    return tenantService.create(request);
  }

  // Maximum of 10 or 20 tenants so limit offset approach should be good enough.
  @GetMapping(TENANTS_ENDPOINT)
  @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
  public TenantListResponse findAll(@Min (MIN_PER_PAGE) @Max(MAX_PER_PAGE) @RequestParam(name = "per_page", required = false, defaultValue = "50") int perPage,
                                    @Min (1) @Max (MAX_PER_PAGE) @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
    return tenantService.findAll(page, perPage);
  }

  @GetMapping(SINGLE_TENANT_ENDPOINT)
  @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
  public TenantCreateResponse findByName(@PathVariable(NAME) String name) {
    return tenantService.findByName(name);
  }

  @PutMapping(SINGLE_TENANT_ENDPOINT)
  @PreAuthorize("hasRole('SYSTEM')")
  public TenantCreateResponse update(@PathVariable(NAME) String name,
                                     @Valid @RequestBody TenantUpdateRequest request) {
    return tenantService.update(request, name);
  }

  @DeleteMapping(SINGLE_TENANT_ENDPOINT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('SYSTEM')")
  public void softDelete(@PathVariable(NAME) String name) {
    tenantService.softDelete(name);
  }

  @PatchMapping(TENANTS_RESTORE_ENDPOINT)
  @PreAuthorize("hasRole('SYSTEM')")
  public TenantCreateResponse restore(@PathVariable(NAME) String name) {
    return tenantService.restore(name);
  }

}
