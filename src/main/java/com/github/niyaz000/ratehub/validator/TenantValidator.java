package com.github.niyaz000.ratehub.validator;

import com.github.niyaz000.ratehub.request.TenantCreateRequest;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;


@Component
@Slf4j
public class TenantValidator {

  public void validate(TenantCreateRequest request) {
    var config = request.getAuthConfig();
    if (config.size() != 2) {
      log.info("tenant found having less than 2 or more than 2 public keys, must provide 2 distinct keys, invalid request");
      throw ExceptionUtil.validationFailedException("public_key", "invalid_public_key_count", "Please provide exactly two unique public key key id - value pairs");
    }
    raiseExceptionIfNotAValidPublicKeyOrOnDuplicateKey(config);
  }

  private void raiseExceptionIfNotAValidPublicKeyOrOnDuplicateKey(Map<String, String> config) {
    config
      .values()
      .forEach(this::validatePublicKey);
  }

  private RSAPublicKey validatePublicKey(String publicKey) {
    try {
      publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");;
      KeyFactory kf = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
      return (RSAPublicKey) kf.generatePublic(keySpecX509);
    } catch (Exception ex) {
      log.warn("encountered exception while validating public key", ex);
      throw ExceptionUtil.validationFailedException("public_key", "invalid_public_key", "Please provide a valid public key");
    }
  }

}
