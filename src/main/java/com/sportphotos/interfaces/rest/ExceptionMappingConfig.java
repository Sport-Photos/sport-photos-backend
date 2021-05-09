package com.sportphotos.interfaces.rest;

import com.sportphotos.domain.ResourceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionMappingConfig {

  private static final String EXCEPTION_MESSAGE_LOG_TEMPLATE =
      "Request processing failed with exception";

  @ResponseBody
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<RestError> handle(ResourceNotFoundException exception) {
    log.error(EXCEPTION_MESSAGE_LOG_TEMPLATE, exception);
    return new ResponseEntity<>(RestError.create(exception.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestError> handle(Exception exception) {
    log.error(EXCEPTION_MESSAGE_LOG_TEMPLATE, exception);
    return new ResponseEntity<>(
        RestError.create(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Getter
  @RequiredArgsConstructor
  private static class RestError {

    private final String message;

    static RestError create(String message) {
      return new RestError(message);
    }
  }
}
