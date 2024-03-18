package com.github.niyaz000.ratehub.repository;

import com.github.niyaz000.ratehub.model.Rating;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

  Optional<Rating> findByTenantIdAndId(long tenantId, long id);

  Optional<Rating> findByTenantIdAndIdAndUserId(long tenantId, long id, String userId);

  Page<Rating> findAllByTenantIdAndUserIdOrderByCreatedAt(Long tenantId, String userId, Pageable page);

  @Modifying
  @Transactional
  void deleteByTenantIdAndId(Long tenantId, Long id);
}
