package com.github.niyaz000.ratehub.auth;

import com.github.niyaz000.ratehub.dao.TenantDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtKeyManager {

  public static final int PER_PAGE = 100;

  private final Map<String, String> jwtKeyMap;

  public JwtKeyManager(@Value("#{${system.auth.keys}}") Map<String,String> systemAuthKeys,
                       TenantDao tenantDao) {
    Map<String, String> keys = new HashMap<>(systemAuthKeys);
    int page = 0;
    boolean hasMoreEntries;
    do {
      var entries = tenantDao.findAll(page, PER_PAGE);
      hasMoreEntries = entries.hasNext();
      entries.stream().forEach(tenant -> {
        tenant.getSigningKeyConfig().forEach((key, value) -> {
          var keyName = getKey(tenant.getName(), key);
          keys.put(keyName, value);
        });
      });
      ++page;
    }  while (hasMoreEntries);
    jwtKeyMap = Collections.unmodifiableMap(keys);
  }

  public Optional<String> getSigningKey(String tenantName,
                                        String keyId) {
    return Optional.ofNullable(jwtKeyMap.get(getKey(tenantName, keyId)));
  }

  private String getKey(String tenantName,
                        String keyId) {
    return tenantName + "_" + keyId;
  }

}
