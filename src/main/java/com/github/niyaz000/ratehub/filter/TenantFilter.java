package com.github.niyaz000.ratehub.filter;

import com.github.niyaz000.ratehub.auth.AuthContext;
import com.github.niyaz000.ratehub.constants.Routes;
import com.github.niyaz000.ratehub.dao.TenantDao;
import com.github.niyaz000.ratehub.dto.RequestContext;
import com.github.niyaz000.ratehub.exception.RestResponseExceptionHandler;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import static com.github.niyaz000.ratehub.constants.JwtConstants.TENANT_NAME;

@RequiredArgsConstructor
@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class TenantFilter extends OncePerRequestFilter {

  private final TenantDao tenantDao;

  private final RestResponseExceptionHandler exceptionHandler;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) {
    var tenantName = AuthContext.getTenantName();
    var tenant = tenantDao.findByName(tenantName);
    if (tenant.isDeleted()) {
      log.warn("tenant with name '{}' is deleted, restricting api usage", tenant.getName());
      exceptionHandler.handleTenantNotFoundException(request, response, ExceptionUtil.tenantNotFoundException(TENANT_NAME));
    } else {
      RequestContext.execute(() -> filter(request, response, filterChain), tenant);
    }
  }

  @SneakyThrows
  private static void filter(HttpServletRequest request,
                             HttpServletResponse response,
                             FilterChain filterChain) {
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getServletPath().contains(Routes.ACTUATOR_ENDPOINT) || AuthContext.isSystemUser();
  }

}
