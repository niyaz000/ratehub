package com.github.niyaz000.ratehub.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {

  private final ObjectMapper mapper;

  @SneakyThrows
  public String toJsonString(Object value) {
    return mapper.writeValueAsString(value);
  }

  @SneakyThrows
  public <T> T fromJsonString(String value, Class<T> clazz) {
    return mapper.convertValue(value, clazz);
  }

}
