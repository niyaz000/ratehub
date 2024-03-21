package com.github.niyaz000.ratehub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class JedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Value("${spring.redis.password}")
  private String password;

  @Value("${spring.redis.timeout}")
  private int timeout;

  @Value("${spring.redis.pool.max-active}")
  private int maxActive;

  @Value("${spring.redis.pool.max-idle}")
  private int maxIdle;

  @Value("${spring.redis.pool.min-idle}")
  private int minIdle;

  @Value("${spring.redis.pool.max-wait}")
  private long maxWaitMillis;

  @Bean
  public JedisPool jedisPool() {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(maxActive);
    config.setMaxIdle(maxIdle);
    config.setMinIdle(minIdle);
    config.setMaxWait(Duration.ofMillis(maxWaitMillis));
    return new JedisPool(config, host, port, timeout, password);
  }

}
