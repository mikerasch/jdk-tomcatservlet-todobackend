package com.michael;

import com.michael.servlet.router.RouterServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.util.Set;

public class App {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.setBaseDir("temp");

        Context ctx = tomcat.addContext("", System.getProperty("java.io.tmpdir"));

        RouterServlet routerServlet = new RouterServlet(Set.of());
        Tomcat.addServlet(ctx, "routerServlet", routerServlet);

        tomcat.start();
        tomcat.getServer().await();
    }
}
