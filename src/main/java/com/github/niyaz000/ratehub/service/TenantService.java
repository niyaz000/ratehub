package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dao.TenantDao;
import com.github.niyaz000.ratehub.mapper.TenantMapper;
import com.github.niyaz000.ratehub.request.TenantCreateRequest;
import com.github.niyaz000.ratehub.request.TenantUpdateRequest;
import com.github.niyaz000.ratehub.response.TenantCreateResponse;
import com.github.niyaz000.ratehub.response.TenantListResponse;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {

  private static final String NAME = "name";

  private final TenantDao tenantDao;

  private final TenantMapper tenantMapper;

  public TenantCreateResponse findByName(String name) {
    log.info("looking up tenant with name '{}'", name);
    var tenant = tenantDao.findByName(name);
    return tenantMapper.mapTenantCreateRequestToResponse(tenant);
  }

  public TenantCreateResponse create(TenantCreateRequest request) {
    log.info("creating tenant for request {}", request);
    raiseExceptionIfTenantAlreadyExists(request);
    try {
      return createTenant(request);
    } catch (DataIntegrityViolationException ex) {
      log.info("encountered DataIntegrityViolationException while processing request {}", request);
      throw ExceptionUtil.duplicateEntityException(request.getName(), NAME, ex);
    }
  }

  public TenantCreateResponse update(TenantUpdateRequest request, String name) {
    log.info("updating tenant for request {}", request);
    try {
      var existingTenant = tenantDao.findByName(name);
      var tenant = tenantMapper.mapTenantUpdateRequestToTenant(request, existingTenant);
      tenant = tenantDao.save(tenant);
      return tenantMapper.mapTenantCreateRequestToResponse(tenant);
    } catch (ObjectOptimisticLockingFailureException ex) {
      throw ExceptionUtil.concurrentUpdateException(name, NAME, ex);
    }
  }

  public void softDelete(String name) {
    log.info("processing soft delete request for tenant {}", name);
    var tenant = tenantDao.findByName(name);
    tenant.setDeleted(true);
    tenantDao.save(tenant);
  }

  public TenantListResponse findAll(int page, int perPage) {
    log.info("listing all tenants with page {} and per_page {}", page, perPage);
    var tenants = tenantDao.findAll(page - 1, perPage);
    return tenantMapper.mapTenantsToTenantListResponse(tenants);
  }

  public TenantCreateResponse restore(String name) {
    log.info("processing restore request for tenant {}", name);
    var tenant = tenantDao.findByName(name);
    tenant.setDeleted(false);
    tenantDao.save(tenant);
    return tenantMapper.mapTenantCreateRequestToResponse(tenant);
  }

  private TenantCreateResponse createTenant(TenantCreateRequest request) {
    var tenant = tenantMapper.mapTenantCreateRequestToTenant(request);
    log.info("going to create entity with params {}", tenant);
    tenant = tenantDao.save(tenant);
    log.info("created entity {}", tenant);
    return tenantMapper.mapTenantCreateRequestToResponse(tenant);
  }

  private void raiseExceptionIfTenantAlreadyExists(TenantCreateRequest request) {
    if (tenantDao.existsByName(request.getName())) {
      throw ExceptionUtil.duplicateEntityException(NAME, request.getName());
    }
  }

}
