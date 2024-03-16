package com.github.niyaz000.ratehub.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.niyaz000.ratehub.constants.ApiErrorConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

  private final ObjectMapper objectMapper;

  @ExceptionHandler(DuplicateEntityException.class)
  public ResponseEntity<Object> handleDuplicateEntityException(DuplicateEntityException ex) {
    var error = new ErrorResponse.Error(ex.getField(), ex.getValue(), DUPLICATE_ENTITY, DUPLICATE_ENTITY_MESSAGE);
    return constructErrorResponse(HttpStatus.BAD_REQUEST, ERROR_CODE, ERROR_MESSAGE, List.of(error));
  }

  @ExceptionHandler(ConcurrentUpdateException.class)
  public ResponseEntity<Object> handleConcurrentUpdateException(ConcurrentUpdateException ex) {
    var error = new ErrorResponse.Error(ex.getField(), ex.getValue(), CONCURRENT_MODIFICATION, CONCURRENT_MODIFICATION_MESSAGE);
    return constructErrorResponse(HttpStatus.BAD_REQUEST, ERROR_CODE, ERROR_MESSAGE, List.of(error));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    return constructErrorResponse(HttpStatus.FORBIDDEN, FORBIDDEN, FORBIDDEN_MESSAGE, List.of());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {
    var errors = constructErrorFromBindingResults(ex.getBindingResult());
    return constructErrorResponse(HttpStatus.BAD_REQUEST, ERROR_CODE, ERROR_MESSAGE, errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
                                                          WebRequest request) {
    var errors = constructValidationErrors(ex);
    return constructErrorResponse(HttpStatus.BAD_REQUEST, ERROR_CODE, ERROR_MESSAGE, errors);
  }

  private List<ErrorResponse.Error> constructNotFoundError(TenantNotFoundException ex) {
    return List.of(new ErrorResponse.Error(ex.getField(), ex.getValue(), NOT_FOUND_ERROR_CODE, TENANT_NOT_FOUND_ERROR_MESSAGE));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnCaughtExceptions(Exception ex,
                                                         WebRequest request) {
    return constructErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
      HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
      INTERNAL_SERVER_ERROR_MESSAGE,
      List.of());
  }

  private List<ErrorResponse.Error> constructErrorFromBindingResults(BindingResult bindingResult) {
    return bindingResult.getFieldErrors()
      .stream()
      .map(error -> new ErrorResponse.Error(error.getField(), Objects.toString(error.getRejectedValue()), error.getCode(), error.getDefaultMessage()))
      .collect(Collectors.toList());
  }

  private List<ErrorResponse.Error> constructValidationErrors(ConstraintViolationException ex) {
    return ex.getConstraintViolations().stream()
            .map(error -> new ErrorResponse.Error(getLeafNode(error.getPropertyPath()), error.getInvalidValue().toString(), INVALID_VALUE,
                    error.getMessage()))
            .collect(Collectors.toList());
  }

  private String getLeafNode(Path path) {
    Iterator<Path.Node> nodes = path.iterator();
    String fieldName = null;
    while (nodes.hasNext()) {
      fieldName = nodes.next().getName();
    }
    return fieldName;
  }

  private ResponseEntity<Object> constructErrorResponse(HttpStatus status,
                                                        String code,
                                                        String errorMessage,
                                                        List<ErrorResponse.Error> errors) {
    var errorResponse = new ErrorResponse(code, errorMessage, errors);
    return ResponseEntity.status(status)
            .body(errorResponse);
  }


  @SneakyThrows
  public void handleUnAuthenticatedRequest(HttpServletRequest request, HttpServletResponse response) {
    var body = objectMapper.writeValueAsString(new ErrorResponse(UNAUTHORIZED, UNAUTHORIZED_MESSAGE, List.of()));
    setResponse(HttpStatus.UNAUTHORIZED.value(), response, body);
  }

  private static void setResponse(int code, HttpServletResponse response, String body) throws IOException {
    response.setStatus(code);
    response.setContentType(APPLICATION_JSON.toString());
    response.getWriter().write(body);
  }

  @SneakyThrows
  public void handleTenantNotFoundException(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              TenantNotFoundException ex) {
    var body = objectMapper.writeValueAsString(constructNotFoundError(ex));
    setResponse(HttpStatus.NOT_FOUND.value(), response, body);
  }

}
