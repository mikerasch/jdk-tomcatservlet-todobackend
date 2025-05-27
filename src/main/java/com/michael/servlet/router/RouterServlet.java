package com.michael.servlet.router;

import com.michael.servlet.TomcatServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class RouterServlet extends HttpServlet {
  private final Set<TomcatServlet> tomcatServlets;

  public RouterServlet(Set<TomcatServlet> tomcatServlets) {
    this.tomcatServlets = tomcatServlets;
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    TomcatServlet servlet =
        tomcatServlets.stream()
            .filter(tomcatServlet -> tomcatServlet.getRoute().handle(req, resp))
            .findFirst()
            .orElse(null);

    // Found the route, return
    if (servlet != null) {
      return;
    }

    // Otherwise 404 bad
    resp.sendError(404);
  }
}
