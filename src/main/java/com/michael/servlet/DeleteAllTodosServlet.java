package com.michael.servlet;

import com.michael.database.Database;
import com.michael.exceptions.UncheckedSqlException;
import com.michael.http.HttpMethod;
import com.michael.http.HttpStatus;
import com.michael.servlet.router.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class DeleteAllTodosServlet extends TomcatServlet {
  private final transient Database database;

  public DeleteAllTodosServlet(Database database) {
    this.database = database;
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    try (var connection = database.dataSource().getConnection()) {
      connection.prepareStatement("DELETE * FROM todo").execute();
      resp.setStatus(HttpStatus.NO_CONTENT.getCode());
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  @Override
  public Route getRoute() {
    return new Route(HttpMethod.DELETE, Pattern.compile("^/$"), this::doDelete);
  }
}
