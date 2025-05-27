package com.michael.http;

public enum HttpStatus {
  OK(200),
  CREATED(201),
  BAD_REQUEST(400),
  NOT_FOUND(404),
  NO_CONTENT(204);

  private final int code;

  HttpStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
