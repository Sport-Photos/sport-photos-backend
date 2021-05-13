package com.sportphotos.domain;

import static java.lang.String.format;

import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public static Supplier<ResourceNotFoundException> notFound(String what, String id) {
    return () -> new ResourceNotFoundException(format("%s - [%s] - not found.", what, id));
  }

  public static Supplier<ResourceNotFoundException> notFoundFor(
      String what, String whatId, String forWhat, String forWhatId) {
    return () ->
        new ResourceNotFoundException(
            format("%s - [%s] - not found for %s - [%s]", what, whatId, forWhat, forWhatId));
  }
}
