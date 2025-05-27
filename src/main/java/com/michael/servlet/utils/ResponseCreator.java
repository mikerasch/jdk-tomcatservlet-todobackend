package com.michael.servlet.utils;

import com.michael.http.HttpStatus;
import com.michael.http.MediaType;
import com.michael.provider.GSONProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ResponseCreator {
  private ResponseCreator() {}

  public static <T> void writeJsonContent(
      T body, HttpServletResponse response, HttpStatus httpStatus) {
    response.setStatus(httpStatus.getCode());
    response.setContentType(MediaType.APPLICATION_JSON.getType());
    response.setCharacterEncoding(StandardCharsets.UTF_8);
    String json = GSONProvider.getGson().toJson(body);

    try {
      response.getWriter().write(json);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
