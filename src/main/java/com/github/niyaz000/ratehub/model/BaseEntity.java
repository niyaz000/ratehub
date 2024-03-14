package com.github.niyaz000.ratehub.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private Date updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

}
