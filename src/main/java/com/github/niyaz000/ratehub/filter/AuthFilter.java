package com.github.niyaz000.ratehub.filter;

import com.github.niyaz000.ratehub.auth.JWTAuthenticator;
import com.github.niyaz000.ratehub.dto.JwtToken;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.github.niyaz000.ratehub.constants.JwtConstants.*;

@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

  private final JWTAuthenticator authenticator;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    authenticator.getToken(request)
            .ifPresentOrElse(token -> {
              var jwt = authenticator.decode(token);
              setRequestAttributes(request, jwt);
            }, () -> {
              throw ExceptionUtil.authenticationFailureException();
            });
    filterChain.doFilter(request, response);
  }

  private static void setRequestAttributes(HttpServletRequest request, JwtToken jwt) {
    request.setAttribute(TENANT_NAME, jwt.getTenantName());
    request.setAttribute(USER_ID, jwt.getUserId());
    request.setAttribute(ROLE, jwt.getRole());
  }

}
