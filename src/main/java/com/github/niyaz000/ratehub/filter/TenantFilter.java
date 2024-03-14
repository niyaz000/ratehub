package com.github.niyaz000.ratehub.filter;

import com.github.niyaz000.ratehub.dao.TenantDao;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.github.niyaz000.ratehub.constants.JwtConstants.TENANT_NAME;
import static com.github.niyaz000.ratehub.constants.Routes.TENANTS_ENDPOINT;

@RequiredArgsConstructor
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

  private final TenantDao tenantDao;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    var tenantName = (String) request.getAttribute(TENANT_NAME);
    var tenant = tenantDao.findByName(tenantName);
    if (tenant.isDeleted()) {
      log.warn("tenant with name '{}' is deleted, restricting api usage", tenant.getName());
      throw ExceptionUtil.tenantNotFoundException(TENANT_NAME);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getMethod().equals(HttpMethod.POST.name()) && request.getServletPath().contains(TENANTS_ENDPOINT);
  }

}
