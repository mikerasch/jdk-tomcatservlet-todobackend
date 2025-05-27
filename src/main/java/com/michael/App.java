package com.michael;

import com.michael.database.Database;
import com.michael.database.Scripts;
import com.michael.filter.CorsFilter;
import com.michael.servlet.DeleteAllTodosServlet;
import com.michael.servlet.DeleteTodoServlet;
import com.michael.servlet.GetAllTodosServlet;
import com.michael.servlet.GetTodoServlet;
import com.michael.servlet.PatchTodoServlet;
import com.michael.servlet.PostTodoServlet;
import com.michael.servlet.TomcatServlet;
import com.michael.servlet.router.RouterServlet;
import java.util.Set;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

public class App {
  public static void main(String[] args) throws Exception {
    Database database = new Database();
    Scripts.init(database);
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8080);
    tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
    tomcat.getConnector();
    Context ctx = tomcat.addContext("", System.getProperty("java.io.tmpdir"));
    addFilters(ctx);
    addServlets(ctx, database);

    tomcat.start();
    tomcat.getServer().await();
  }

  private static void addServlets(Context ctx, Database database) {
    Set<TomcatServlet> tomcatServlets =
        Set.of(
            new DeleteAllTodosServlet(database),
            new DeleteTodoServlet(database),
            new GetAllTodosServlet(database),
            new GetTodoServlet(database),
            new PatchTodoServlet(database),
            new PostTodoServlet(database));
    RouterServlet routerServlet = new RouterServlet(tomcatServlets);
    Tomcat.addServlet(ctx, "routerServlet", routerServlet);
    ctx.addServletMappingDecoded("/*", "routerServlet");
  }

  private static void addFilters(Context ctx) {
    FilterDef filterDef = new FilterDef();
    filterDef.setFilterName("cors");
    filterDef.setFilterClass(CorsFilter.class.getName());
    ctx.addFilterDef(filterDef);

    FilterMap filterMap = new FilterMap();
    filterMap.setFilterName("cors");
    filterMap.addURLPattern("/*");
    ctx.addFilterMap(filterMap);
  }
}
