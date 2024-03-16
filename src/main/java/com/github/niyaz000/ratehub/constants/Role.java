package com.github.niyaz000.ratehub.constants;

public enum Role {
  SYSTEM("system"),
  ADMIN("admin"),
  USER("user");

  Role(String value) {
    this.value = value;
  }
  private String value;
}
