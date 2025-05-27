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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GetAllTodosServlet extends TomcatServlet {
  private final transient Database database;

  public GetAllTodosServlet(Database database) {
    this.database = database;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try (var con = database.dataSource().getConnection()) {
      PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM todo");
      List<TodoResponse> todoResponses =
          mapFromResultSet(preparedStatement.executeQuery()).stream()
              .map(TodoResponse::new)
              .toList();
      ResponseCreator.writeJsonContent(todoResponses, resp, HttpStatus.OK);
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  private List<TodoEntity> mapFromResultSet(ResultSet resultSet) throws SQLException {
    List<TodoEntity> todoEntities = new ArrayList<>();
    while (resultSet.next()) {
      todoEntities.add(new TodoEntity(resultSet));
    }
    return todoEntities;
  }

  @Override
  public Route getRoute() {
    return new Route(HttpMethod.GET, Pattern.compile("^/$"), this::doGet);
  }
}
