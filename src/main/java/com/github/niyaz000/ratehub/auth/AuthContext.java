package com.github.niyaz000.ratehub.auth;

import com.github.niyaz000.ratehub.constants.Role;
import com.github.niyaz000.ratehub.dto.JwtToken;

import java.util.Objects;

public final class AuthContext {

  private static final ThreadLocal<JwtToken> context = new ThreadLocal<>();

  public static void execute(Runnable runnable, JwtToken jwtToken) {
    try {
      context.set(jwtToken);
      runnable.run();
    } finally {
      context.remove();
    }
  }

  private static JwtToken getContext() {
    var ctxt = context.get();
    return ctxt;
  }

  public static Role getRole() {
    return getContext().getRole();
  }

  public static String getUserId() {
    return getContext().getUserId();
  }

  public static String getTenantName() {
    return getContext().getTenantName();
  }

  public static boolean isSystemUser() {
    return Objects.equals(getContext().getRole(), Role.SYSTEM);
  }
}
