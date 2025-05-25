package com.michael;

import com.michael.filter.CorsFilter;
import com.michael.servlet.DeleteAllTodosServlet;
import com.michael.servlet.DeleteTodoServlet;
import com.michael.servlet.GetAllTodosServlet;
import com.michael.servlet.GetTodoServlet;
import com.michael.servlet.PatchTodoServlet;
import com.michael.servlet.PostTodoServlet;
import com.michael.servlet.TomcatServlet;
import com.michael.servlet.router.RouterServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.util.Set;

public class App {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir("temp");
        Context ctx = tomcat.addContext("", System.getProperty("java.io.tmpdir"));

        addFilters(ctx);
        addServlets(ctx);

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void addServlets(Context ctx) {
        Set<TomcatServlet> tomcatServlets = Set.of(
           new DeleteAllTodosServlet(),
           new DeleteTodoServlet(),
           new GetAllTodosServlet(),
           new GetTodoServlet(),
           new PatchTodoServlet(),
           new PostTodoServlet()
        );
        RouterServlet routerServlet = new RouterServlet(tomcatServlets);
        Tomcat.addServlet(ctx, "routerServlet", routerServlet);
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
