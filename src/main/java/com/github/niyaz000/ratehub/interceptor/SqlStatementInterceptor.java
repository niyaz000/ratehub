package com.github.niyaz000.ratehub.interceptor;

import com.github.niyaz000.ratehub.util.LoggerUtil;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class SqlStatementInterceptor implements StatementInspector {

  @Override
  public String inspect(String sql) {
    return sql + " /* " + LoggerUtil.traceId() + " */";
  }

}
