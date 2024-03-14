package com.github.niyaz000.ratehub.auth;

import com.github.niyaz000.ratehub.dto.JwtToken;

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

  public String getRole() {
    return context.get().getRole();
  }

  public String getUserId() {
    return context.get().getUserId();
  }

  public String getTenantName() {
    return context.get().getTenantName();
  }

}
