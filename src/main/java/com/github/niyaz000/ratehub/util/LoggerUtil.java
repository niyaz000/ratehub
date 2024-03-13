package com.github.niyaz000.ratehub.util;

import io.opentelemetry.javaagent.shaded.io.opentelemetry.api.trace.Span;
import org.slf4j.MDC;

import java.util.UUID;

import static io.opentelemetry.javaagent.shaded.io.opentelemetry.api.internal.ConfigUtil.defaultIfNull;

public final class LoggerUtil {

  public static void withMDC(Runnable runnable, String key, String value) {
    try {
      MDC.put(key, value);
      runnable.run();
    } finally {
      MDC.remove(key);
    }
  }

  public static String getRequestId(String headerValue) {
    return defaultIfNull(headerValue, UUID.randomUUID().toString());
  }

  public static String traceId() {
    return Span.current().getSpanContext().getTraceId();
  }

}
