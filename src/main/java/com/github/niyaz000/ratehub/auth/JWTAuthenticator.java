package com.github.niyaz000.ratehub.auth;

import com.github.niyaz000.ratehub.constants.JwtConstants;
import com.github.niyaz000.ratehub.dto.JwtToken;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Optional;

import static io.opentelemetry.javaagent.shaded.io.opentelemetry.api.internal.ConfigUtil.defaultIfNull;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticator {

  private static final Long CLOCK_SKEW_SECONDS = 10L;

  private final JwtKeyManager jwtKeyManager;

  public static final String TOKEN_PREFIX = "Bearer ";

  public static final String HEADER_STRING = "Authorization";

  public static final String KID = "kid";

  public Optional<String> getToken(HttpServletRequest req) {
    String token = req.getHeader(HEADER_STRING);
    String headerValue = ((token != null && token.startsWith(TOKEN_PREFIX)) ? token.replace(TOKEN_PREFIX, "") : null);
    return Optional.ofNullable(headerValue);
  }

  private Optional<PublicKey> getPublicKeyForKeyId(String jwtToken) {
    var tokenWithoutSign = jwtToken.substring(0, jwtToken.lastIndexOf('.') + 1);
    var claims = Jwts
      .parser()
      .parseClaimsJwt(tokenWithoutSign);

    var kid = claims.getHeader().get(KID).toString();
    var tenantName = defaultIfNull(claims.getBody().getIssuer(), Strings.EMPTY);
    return jwtKeyManager.getPublicKeyForKeyId(tenantName, kid);
  }

  public JwtToken decode(String jwtToken) {
    var key = getPublicKeyForKeyId(jwtToken).orElseThrow(ExceptionUtil::authenticationFailureException);
    var body = Jwts
            .parser()
            .setAllowedClockSkewSeconds(CLOCK_SKEW_SECONDS)
            .setSigningKey(key)
            .parseClaimsJws(jwtToken)
            .getBody();
    return new JwtToken(body.getIssuer(), body.get(JwtConstants.ROLE, String.class), body.get(JwtConstants.USER_ID, String.class));
  }

}
