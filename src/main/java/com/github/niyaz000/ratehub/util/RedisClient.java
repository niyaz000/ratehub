package com.github.niyaz000.ratehub.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisClient {

  private final JedisPool jedisPool;

  public boolean setNex(String key,
                        String value,
                        Duration duration) {
    try (var jedis = jedisPool.getResource()) {
      return "OK".equals(jedis.set(key, value, SetParams.setParams().nx().ex(duration.getSeconds())));
    }
  }

  public long deleteKey(String key) {
    try (var jedis = jedisPool.getResource()) {
      return jedis.del(key);
    }
  }

  public List<String> zrange(String key, int start, int stop) {
    try (var jedis = jedisPool.getResource()) {
      return jedis.zrange(key, start, stop);
    }
  }
}
