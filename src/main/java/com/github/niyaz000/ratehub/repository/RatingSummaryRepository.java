package com.github.niyaz000.ratehub.repository;

import com.github.niyaz000.ratehub.model.RatingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingSummaryRepository extends JpaRepository<RatingSummary, Long> {

  Optional<RatingSummary> findByTenantIdAndProductId(long tenantId, String productId);

}
