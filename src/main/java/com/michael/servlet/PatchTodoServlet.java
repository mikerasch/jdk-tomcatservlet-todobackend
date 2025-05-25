package com.michael.servlet;

import com.michael.http.HttpMethod;
import com.michael.servlet.router.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.regex.Pattern;

public class PatchTodoServlet extends TomcatServlet {

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp)  {

    }

    @Override
    public Route getRoute() {
        return new Route(HttpMethod.PATCH, Pattern.compile("^\\\\d+$"), this::doPatch);
    }
}
