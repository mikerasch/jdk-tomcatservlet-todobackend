package com.michael.servlet;

import com.michael.database.Database;
import com.michael.database.entities.TodoEntity;
import com.michael.exceptions.UncheckedSqlException;
import com.michael.http.HttpMethod;
import com.michael.http.HttpStatus;
import com.michael.models.TodoRequest;
import com.michael.models.TodoResponse;
import com.michael.provider.GSONProvider;
import com.michael.servlet.router.Route;
import com.michael.servlet.utils.ResponseCreator;
import com.michael.validators.TodoValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

public class PostTodoServlet extends TomcatServlet {
  private final transient Database database;

  public PostTodoServlet(Database database) {
    this.database = database;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    TodoRequest todoRequest;
    try {
      todoRequest = GSONProvider.getGson().fromJson(req.getReader(), TodoRequest.class);
      boolean isValid = TodoValidator.validateTodoRequestPost(todoRequest);
      if (!isValid) {
        resp.sendError(HttpStatus.BAD_REQUEST.getCode());
        return;
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    try (var con = database.dataSource().getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement("INSERT INTO todo (title, completed, \"order\") VALUES (?, ?, ?) RETURNING *");
      preparedStatement.setString(1, todoRequest.title());
      preparedStatement.setBoolean(2, Optional.ofNullable(todoRequest.completed()).orElse(false));
      preparedStatement.setInt(3, Optional.ofNullable(todoRequest.order()).orElse(0));
      ResultSet resultSet = preparedStatement.executeQuery();
      TodoEntity entity = mapFromResultSet(resultSet);
      ResponseCreator.writeJsonContent(new TodoResponse(entity, req), resp, HttpStatus.CREATED);
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  private TodoEntity mapFromResultSet(ResultSet resultSet) throws SQLException {
    resultSet.next();
    return new TodoEntity(resultSet);
  }

  @Override
  public Route getRoute() {
    return new Route(HttpMethod.POST, Pattern.compile("^/$"), this::doPost);
  }
}
