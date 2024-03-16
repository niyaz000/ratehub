package com.github.niyaz000.ratehub.auth;

import com.github.niyaz000.ratehub.dao.TenantDao;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Component
@Slf4j
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

  @SneakyThrows
  public Optional<PublicKey> getPublicKeyForKeyId(String tenantName,
                                                  String keyId) {
      var key = jwtKeyMap.get(getKey(tenantName, keyId));
      if (key == null) {
        log.warn("could not find public key for tenantName {} keyId {}", tenantName, keyId);
        return Optional.empty();
      }
      byte[] keyBytes = Base64.getDecoder().decode(key);
      var publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
      return Optional.ofNullable(publicKey);
  }

  private String getKey(String tenantName,
                        String keyId) {
    return tenantName + "_" + keyId;
  }

}
