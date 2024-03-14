package com.github.niyaz000.ratehub.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ConcurrentUpdateException extends RuntimeException {

  private final String field;

  private final String value;

  public ConcurrentUpdateException(String field, String value, Throwable ex) {
    super(ex);
    this.field = field;
    this.value = value;
  }

}
