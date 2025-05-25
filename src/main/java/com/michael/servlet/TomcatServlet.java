package com.michael.servlet;

import com.michael.servlet.router.Route;
import jakarta.servlet.http.HttpServlet;

public abstract class TomcatServlet extends HttpServlet {
    public abstract Route getRoute();
}
