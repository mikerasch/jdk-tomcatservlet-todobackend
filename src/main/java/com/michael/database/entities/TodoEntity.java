package com.michael.database.entities;

import com.michael.exceptions.UncheckedSqlException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoEntity {
  private int id;
  private String title;
  private boolean completed;
  private int order;

  public TodoEntity(ResultSet resultSet) {
    try {
      setId(resultSet.getInt("id"));
      setTitle(resultSet.getString("title"));
      setCompleted(resultSet.getBoolean("completed"));
      setOrder(resultSet.getInt("order"));
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public boolean isCompleted() {
    return completed;
  }

  public int getOrder() {
    return order;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}
