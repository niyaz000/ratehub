package com.github.niyaz000.ratehub.dao;

import com.github.niyaz000.ratehub.model.Tenant;
import com.github.niyaz000.ratehub.repository.TenantRepository;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantDao {

  private final TenantRepository repository;

  public Tenant findByName(String name) {
    return repository.findByName(name).orElseThrow(() -> ExceptionUtil.tenantNotFoundException(name));
  }

  public boolean existsByName(String name) {
    return repository.findByName(name).isPresent();
  }

  public Tenant save(Tenant tenant) {
    return repository.save(tenant);
  }

  public Page<Tenant> findAll(int page, int perPage) {
    return repository.findAll(PageRequest.of(page, perPage));
  }

}
