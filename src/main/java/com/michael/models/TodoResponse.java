package com.michael.models;

import com.michael.database.entities.TodoEntity;

public record TodoResponse(int id, String title, boolean completed, int order) {
  public TodoResponse(TodoEntity entity) {
    this(entity.getId(), entity.getTitle(), entity.isCompleted(), entity.getOrder());
  }
}
