package com.github.niyaz000.ratehub.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RatingChangeDto {

  private final UUID txnId;

  private final long tenantId;

}
