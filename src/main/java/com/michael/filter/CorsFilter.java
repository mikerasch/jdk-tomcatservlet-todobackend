package com.michael.filter;

import com.michael.http.HttpMethod;
import com.michael.http.HttpStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    response.setHeader("access-control-allow-origin", "*");

    HttpMethod method = HttpMethod.valueOf(request.getMethod());

    if (method == HttpMethod.OPTIONS) {
      response.setHeader("access-control-allow-methods", "*");
      response.setHeader("access-control-allow-headers", "*");
      response.setHeader("access-control-max-age", "86400");
      response.setStatus(HttpStatus.OK.getCode());
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }
}
