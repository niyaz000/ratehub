package com.github.niyaz000.ratehub.controller;

import com.github.niyaz000.ratehub.auth.AuthContext;
import com.github.niyaz000.ratehub.constants.AppConstants;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.github.niyaz000.ratehub.constants.Routes.SINGLE_RATINGS_ENDPOINT;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class RatingController {

  private final RatingService ratingService;

  @GetMapping(SINGLE_RATINGS_ENDPOINT)
  public RatingGetResponse findById(@PathVariable(AppConstants.ID) Long id) {
    return ratingService.findById(AuthContext.getTenantName(), id);
  }


}
