package com.michael.http;

public enum MediaType {
  APPLICATION_JSON("application/json");

  private final String type;

  MediaType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
