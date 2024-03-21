package com.github.niyaz000.ratehub.dto;

import com.github.niyaz000.ratehub.model.Rating;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class RatingChangeDto {

  private Rating oldValue;

  private Rating newValue;

  public boolean isCreated() {
    return Objects.isNull(oldValue) && !Objects.isNull(newValue);
  }

  public boolean isModified() {
    return !Objects.isNull(oldValue) && !Objects.isNull(newValue);
  }

  public boolean isRemoved() {
    return !Objects.isNull(oldValue) && Objects.isNull(newValue);
  }

}
