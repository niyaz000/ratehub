package com.github.niyaz000.ratehub.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidationFailedException extends RuntimeException {
  private final String field;

  private final String value;

  private final String message;

}
