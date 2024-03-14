package com.github.niyaz000.ratehub.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {

  private final String field;

  private final String value;

}
