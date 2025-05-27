package com.michael.servlet;

import com.michael.database.Database;
import com.michael.exceptions.UncheckedSqlException;
import com.michael.http.HttpMethod;
import com.michael.http.HttpStatus;
import com.michael.servlet.router.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class DeleteTodoServlet extends TomcatServlet {
  private final transient Database database;

  public DeleteTodoServlet(Database database) {
    this.database = database;
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    try (var con = database.dataSource().getConnection()) {
      String pathInfo = req.getPathInfo();
      int id = Integer.parseInt(pathInfo.substring(1));
      PreparedStatement statement = con.prepareStatement("DELETE FROM todo WHERE id = ?");
      statement.setInt(1, id);
      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        resp.sendError(
            HttpStatus.NO_CONTENT.getCode(),
            "Record could not be deleted as it was not found in database.");
        return;
      }
      resp.setStatus(HttpStatus.NO_CONTENT.getCode());
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public Route getRoute() {
    return new Route(HttpMethod.DELETE, Pattern.compile("^\\\\d+$"), this::doDelete);
  }
}
