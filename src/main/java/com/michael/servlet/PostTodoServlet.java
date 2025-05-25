package com.michael.servlet;

import com.michael.http.HttpMethod;
import com.michael.servlet.router.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.regex.Pattern;

public class PostTodoServlet extends TomcatServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    public Route getRoute() {
        return new Route(HttpMethod.POST, Pattern.compile("^/$"), this::doPost);
    }
}
