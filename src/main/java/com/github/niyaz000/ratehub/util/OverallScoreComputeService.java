package com.github.niyaz000.ratehub.util;

import com.github.niyaz000.ratehub.dao.RatingsAuditDao;
import com.github.niyaz000.ratehub.dto.RatingChangeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OverallScoreComputeService {

  private final RatingsAuditDao ratingsAuditDao;
  @Async

  public void computeNewScore(RatingChangeDto dto) {
    ratingsAuditDao.findByTenantIdAndTxnId(dto.getTenantId(), dto.getTxnId())
      .ifPresentOrElse(audit -> {
        audit.getAction();
      }, () -> {

      });
  }


}
