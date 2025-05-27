package com.michael.exceptions;

public class UncheckedSqlException extends RuntimeException {
  public UncheckedSqlException(Exception e) {
    super(e);
  }
}
