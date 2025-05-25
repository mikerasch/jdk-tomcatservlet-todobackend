package com.michael.servlet.router;

import com.michael.http.HttpMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Route(HttpMethod method, Pattern pattern, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
    public boolean handle(HttpServletRequest incomingReq, HttpServletResponse incomingResponse) {
        String path = incomingReq.getRequestURI();

        HttpMethod incomingMethod = HttpMethod.valueOf(incomingReq.getMethod());

        if (method != incomingMethod) {
            return false;
        }

        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            this.handler.accept(incomingReq, incomingResponse);
            return true;
        }

        return false;
    }
}
