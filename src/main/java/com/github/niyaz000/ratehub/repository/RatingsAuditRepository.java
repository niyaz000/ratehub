package com.github.niyaz000.ratehub.repository;

import com.github.niyaz000.ratehub.model.RatingsAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingsAuditRepository extends JpaRepository<RatingsAudit, Long> {

  Optional<RatingsAudit> findByTenantIdAndTxnId(long tenantId, UUID txnId);

}