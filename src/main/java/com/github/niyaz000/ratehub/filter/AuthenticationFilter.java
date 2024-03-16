package com.github.niyaz000.ratehub.filter;

import com.github.niyaz000.ratehub.auth.AuthContext;
import com.github.niyaz000.ratehub.auth.JWTAuthenticator;
import com.github.niyaz000.ratehub.constants.Routes;
import com.github.niyaz000.ratehub.dto.JwtToken;
import com.github.niyaz000.ratehub.exception.RestResponseExceptionHandler;
import com.github.niyaz000.ratehub.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

import static com.github.niyaz000.ratehub.constants.JwtConstants.*;

@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JWTAuthenticator authenticator;

  @Autowired
  private RestResponseExceptionHandler exceptionHandler;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) {
    authenticator.getToken(request)
        .ifPresentOrElse(token -> {
          var jwt = verifyTokenAuthenticity(request, response, token);
          jwt.ifPresentOrElse(decodedToken -> {
            setRequestAttributes(request, decodedToken);
            var user = new User(decodedToken.getUserId(), decodedToken.getRole());
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AuthContext.execute(() -> doFilter(request, response, filterChain), decodedToken);
          }, () -> log.warn("token verification failed, please provide a valid token."));
        }, () -> {
          log.warn("request missing authentication header, please add a valid auth header");
          exceptionHandler.handleUnAuthenticatedRequest(request, response);
        });
  }

  private Optional<JwtToken> verifyTokenAuthenticity(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    String token) {
    try {
      return Optional.of(authenticator.decode(token));
    } catch (Exception ex) {
      log.warn("encountered exception while trying to decode token", ex);
      exceptionHandler.handleUnAuthenticatedRequest(request, response);
    }
    return Optional.empty();
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return request.getServletPath().contains(Routes.ACTUATOR_ENDPOINT);
  }

  @SneakyThrows
  private static void doFilter(HttpServletRequest request,
                               HttpServletResponse response,
                               FilterChain filterChain) {
    filterChain.doFilter(request, response);
  }

  private static void setRequestAttributes(HttpServletRequest request, JwtToken jwt) {
    request.setAttribute(TENANT_NAME, jwt.getTenantName());
    request.setAttribute(USER_ID, jwt.getUserId());
    request.setAttribute(ROLE, jwt.getRole());
  }

}
