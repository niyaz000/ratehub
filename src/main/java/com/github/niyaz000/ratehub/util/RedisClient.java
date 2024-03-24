package com.github.niyaz000.ratehub.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
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

  public List<String> zRangeByScore(String key, double start, double stop) {
    try (var jedis = jedisPool.getResource()) {
      return jedis.zrangeByScore(key, start, stop);
    }
  }

  public long zRem(String key, String member) {
    try (var jedis = jedisPool.getResource()) {
      return jedis.zrem(key, member);
    }
  }

  public Transaction getTransaction() {
    try (var jedis = jedisPool.getResource()) {
      return jedis.multi();
    }
  }
}
