package com.github.niyaz000.ratehub.dao;

import com.github.niyaz000.ratehub.model.RatingSummary;
import com.github.niyaz000.ratehub.repository.RatingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RatingSummaryDao {

  private final RatingSummaryRepository repository;

  public Optional<RatingSummary> findByTenantIdAndProductId(long tenantId, String productId) {
    return repository.findByTenantIdAndProductId(tenantId, productId);
  }

  public RatingSummary save(RatingSummary ratingSummary) {
    return repository.save(ratingSummary);
  }

}
