package com.michael.validators;

import com.michael.models.TodoRequest;

public class TodoValidator {
  private TodoValidator() {}

  public static boolean validateTodoRequestPost(TodoRequest todoRequest) {
    return todoRequest.title() != null && !todoRequest.title().trim().isEmpty();
  }

  public static boolean validateTodoRequestPatch(TodoRequest todoRequest) {
    if (todoRequest.title() == null) {
      return true;
    }
    return !todoRequest.title().isBlank();
  }
}
