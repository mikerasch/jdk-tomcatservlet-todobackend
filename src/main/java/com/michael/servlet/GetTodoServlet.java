package com.michael.servlet;

import com.michael.database.Database;
import com.michael.database.entities.TodoEntity;
import com.michael.exceptions.UncheckedSqlException;
import com.michael.http.HttpMethod;
import com.michael.http.HttpStatus;
import com.michael.models.TodoResponse;
import com.michael.servlet.router.Route;
import com.michael.servlet.utils.ResponseCreator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

public class GetTodoServlet extends TomcatServlet {
  private final transient Database database;

  public GetTodoServlet(Database database) {
    this.database = database;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try (var con = database.dataSource().getConnection()) {
      String pathInfo = req.getPathInfo();
      int id = Integer.parseInt(pathInfo.substring(1));
      PreparedStatement statement = con.prepareStatement("SELECT * FROM todo WHERE id = ?");
      statement.setInt(1, id);
      TodoResponse todoResponse =
          mapFromResultSet(statement.executeQuery()).map(TodoResponse::new).orElse(null);
      if (todoResponse == null) {
        resp.sendError(HttpStatus.NOT_FOUND.getCode(), "Record not found.");
        return;
      }
      ResponseCreator.writeJsonContent(todoResponse, resp, HttpStatus.OK);
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Optional<TodoEntity> mapFromResultSet(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      return Optional.of(new TodoEntity(resultSet));
    }
    return Optional.empty();
  }

  @Override
  public Route getRoute() {
    return new Route(HttpMethod.GET, Pattern.compile("^\\\\d+$"), this::doGet);
  }
}
