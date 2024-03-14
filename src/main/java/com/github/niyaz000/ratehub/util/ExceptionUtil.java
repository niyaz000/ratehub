package com.github.niyaz000.ratehub.util;

import com.github.niyaz000.ratehub.exception.AuthenticationFailureException;
import com.github.niyaz000.ratehub.exception.ConcurrentUpdateException;
import com.github.niyaz000.ratehub.exception.DuplicateEntityException;
import com.github.niyaz000.ratehub.exception.TenantNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public final class ExceptionUtil {

  private static final String NAME = "name";

  private ExceptionUtil() {
  }

  public static DuplicateEntityException duplicateEntityException(String field,
                                                                  String value) {
    return new DuplicateEntityException(field, value);
  }

  public static DuplicateEntityException duplicateEntityException(String field,
                                                                  String value,
                                                                  DataIntegrityViolationException ex) {
    return new DuplicateEntityException(field, value, ex);
  }

  public static ConcurrentUpdateException concurrentUpdateException(String field,
                                                                    String value,
                                                                    ObjectOptimisticLockingFailureException ex) {
    return new ConcurrentUpdateException(field, value, ex);
  }

  public static TenantNotFoundException tenantNotFoundException(String name) {
    return new TenantNotFoundException(NAME, name);
  }

  public static AuthenticationFailureException authenticationFailureException() {
    return new AuthenticationFailureException();
  }

}
