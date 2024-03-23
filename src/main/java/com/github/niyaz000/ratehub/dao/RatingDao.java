package com.github.niyaz000.ratehub.dao;

import com.github.niyaz000.ratehub.model.Rating;
import com.github.niyaz000.ratehub.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RatingDao {

  private final RatingRepository repository;

  public Rating save(Rating rating) {
    return repository.save(rating);
  }

  public Optional<Rating> findByTenantIdAndId(long tenantId, long id) {
    return repository.findByTenantIdAndId(tenantId, id);
  }

  public Optional<Rating> findByTenantIdAndUserIdAndProductId(long tenantId, String userId, String productId) {
    return repository.findByTenantIdAndUserIdAndProductId(tenantId, userId, productId);
  }

  public Optional<Rating> findByTenantIdAndIdAndUserId(long tenantId, long id, String userId) {
    return repository.findByTenantIdAndIdAndUserId(tenantId, id, userId);
  }

  public Page<Rating> findAllByTenantIdAndUserId(Long tenantId, String userId, Pageable page) {
    return repository.findAllByTenantIdAndUserIdOrderByCreatedAt(tenantId, userId, page);
  }

  public void deleteByTenantIdAndId(Long tenantId, Long id) {
    repository.deleteByTenantIdAndId(tenantId, id);
  }

  public Page<Rating> findAllRatingForProduct(Long tenantId, String productId, long lastRecordId, Pageable page) {
    return repository.findAllByTenantIdAndProductIdAndIdGreaterThanOrderByCreatedAt(tenantId, productId, lastRecordId, page);
  }

}
