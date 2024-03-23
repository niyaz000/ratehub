package com.github.niyaz000.ratehub.dto;

import com.github.niyaz000.ratehub.model.Rating;

import java.util.Optional;

public class RatingChangeDto {

  public RatingChangeDto(Rating oldValue, Rating newValue) {
    this.oldValue = Optional.ofNullable(oldValue);
    this.newValue = Optional.ofNullable(newValue);
  }

  private final Optional<Rating> oldValue;

  private final Optional<Rating> newValue;

  public boolean isCreated() {
    return oldValue.isEmpty() && newValue.isPresent();
  }

  public boolean isModified() {
    return oldValue.isPresent() && newValue.isPresent();
  }

  public boolean isRemoved() {
    return oldValue.isPresent() && newValue.isEmpty();
  }

  public Rating getNewValue() {
    return newValue.get();
  }
}
