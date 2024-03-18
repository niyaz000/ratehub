package com.github.niyaz000.ratehub.dto;

import com.github.niyaz000.ratehub.model.Tenant;

public final class RequestContext {
  private static final ThreadLocal<Tenant> context = new ThreadLocal<>();

  public static void execute(Runnable runnable, Tenant tenant) {
    try {
      context.set(tenant);
      runnable.run();
    } finally {
      context.remove();
    }
  }

  public static Tenant getTenant() {
    return context.get();
  }

}
