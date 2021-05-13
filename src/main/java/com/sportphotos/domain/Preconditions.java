package com.sportphotos.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Preconditions {

  private static final String IS_NULL_OR_EMPTY = "%s is null or empty";
  private static final String IS_NULL = "%s is null";

  public static void notNull(Object value, String name) {
    checkArgument(nonNull(value), format(IS_NULL, name));
  }

  public static void notNullOrEmpty(String value, String name) {
    checkArgument(!isNullOrEmpty(value), format(IS_NULL_OR_EMPTY, name));
  }
}
