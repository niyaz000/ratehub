package com.github.niyaz000.ratehub.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ErrorResponse(String errorCode, String errorMessage, List<Error> errors) {

  @Override
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public List<Error> errors() {
    return errors;
  }

  @Data
  @RequiredArgsConstructor
  public static class Error {
    private final String field;
    private final String value;
    private final String code;
    private final String message;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" + "code='" + errorCode + '\'' + ", description='" + errorMessage + '\'' + ", errors=" + errors
            + '}';
  }
}

