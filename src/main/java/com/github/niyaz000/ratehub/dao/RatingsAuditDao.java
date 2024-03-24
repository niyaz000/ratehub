package com.github.niyaz000.ratehub.dao;

import com.github.niyaz000.ratehub.model.RatingsAudit;
import com.github.niyaz000.ratehub.repository.RatingsAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RatingsAuditDao {
  private final RatingsAuditRepository repository;

  public Optional<RatingsAudit> findByTenantIdAndTxnId(long tenantId, UUID txnId) {
    return repository.findByTenantIdAndTxnId(tenantId, txnId);
  }

}
