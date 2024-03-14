package com.github.niyaz000.ratehub.filter;

import com.github.niyaz000.ratehub.util.LoggerUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.github.niyaz000.ratehub.constants.LoggerConstants.X_REQUEST_ID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    var requestId = LoggerUtil.getRequestId(request.getHeader(X_REQUEST_ID));
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    response.setHeader(X_REQUEST_ID, requestId);

    LoggerUtil.withMDC(() -> filter(request, response, filterChain), X_REQUEST_ID, requestId);
  }

  @SneakyThrows
  private void filter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
    chain.doFilter(request, response);
  }

}
