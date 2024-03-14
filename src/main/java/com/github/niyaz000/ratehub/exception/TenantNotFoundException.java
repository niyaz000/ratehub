package com.github.niyaz000.ratehub.exception;

public class TenantNotFoundException extends NotFoundException {

  public TenantNotFoundException(String field, String value) {
    super(field, value);
  }
}
