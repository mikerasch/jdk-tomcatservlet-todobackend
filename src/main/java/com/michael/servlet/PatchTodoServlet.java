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

public class PatchTodoServlet extends TomcatServlet {
  private final transient Database database;

  public PatchTodoServlet(Database database) {
    this.database = database;
  }

  @Override
  protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
    TodoRequest todoRequest;
    try {
      todoRequest = GSONProvider.getGson().fromJson(req.getReader(), TodoRequest.class);
      boolean isValid = TodoValidator.validateTodoRequestPatch(todoRequest);
      if (!isValid) {
        resp.sendError(HttpStatus.BAD_REQUEST.getCode());
        return;
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    try (var con = database.dataSource().getConnection()) {
      String path = req.getPathInfo();
      int id = Integer.parseInt(path.substring(1));
      PreparedStatement selectStatement = con.prepareStatement("SELECT * FROM todo WHERE id = ?");
      selectStatement.setInt(1, id);
      TodoEntity todoEntity = mapFromResultSet(selectStatement.executeQuery()).orElse(null);
      if (todoEntity == null) {
        resp.sendError(HttpStatus.NOT_FOUND.getCode());
        return;
      }
      updateTodoEntity(todoRequest, todoEntity);
      PreparedStatement updateStatement =
          con.prepareStatement(
              "UPDATE todo SET title = ?, completed = ?, \"order\" = ? WHERE id = ?");
      updateStatement.setString(1, todoEntity.getTitle());
      updateStatement.setBoolean(2, todoEntity.isCompleted());
      updateStatement.setInt(3, todoEntity.getOrder());
      updateStatement.setInt(4, id);
      updateStatement.execute();
      TodoResponse todoResponse = new TodoResponse(todoEntity);
      ResponseCreator.writeJsonContent(todoResponse, resp, HttpStatus.OK);
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void updateTodoEntity(TodoRequest todoRequest, TodoEntity todoEntity) {
    Optional.ofNullable(todoRequest.title()).ifPresent(todoEntity::setTitle);
    Optional.ofNullable(todoRequest.completed()).ifPresent(todoEntity::setCompleted);
    Optional.ofNullable(todoRequest.order()).ifPresent(todoEntity::setOrder);
  }

  private Optional<TodoEntity> mapFromResultSet(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      return Optional.of(new TodoEntity(resultSet));
    }
    return Optional.empty();
  }

  @Override
  public Route getRoute() {
    return new Route(HttpMethod.PATCH, Pattern.compile("^/\\d+$"), this::doPatch);
  }
}
