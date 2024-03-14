package com.github.niyaz000.ratehub.repository;

import com.github.niyaz000.ratehub.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

  Optional<Tenant> findByName(String name);

}
