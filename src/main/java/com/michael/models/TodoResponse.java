package com.michael.models;

import com.michael.database.entities.TodoEntity;
import jakarta.servlet.http.HttpServletRequest;

public record TodoResponse(String url, String title, boolean completed, int order) {
  public TodoResponse(TodoEntity entity, HttpServletRequest request) {
    this("https://" + request.getHeader("host") + "/" + entity.getId(), entity.getTitle(), entity.isCompleted(), entity.getOrder());
  }
}
