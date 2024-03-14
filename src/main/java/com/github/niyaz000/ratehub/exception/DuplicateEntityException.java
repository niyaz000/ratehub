package com.github.niyaz000.ratehub.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class DuplicateEntityException extends RuntimeException {

  public DuplicateEntityException(String field, String value, Throwable ex) {
    super(ex);
    this.field = field;
    this.value = value;
  }

  private final String field;

  private final String value;

}
