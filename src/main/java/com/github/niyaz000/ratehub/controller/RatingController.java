package com.github.niyaz000.ratehub.controller;

import com.github.niyaz000.ratehub.auth.AuthContext;
import com.github.niyaz000.ratehub.constants.AppConstants;
import com.github.niyaz000.ratehub.dto.RequestContext;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.response.UserRatingsResponse;
import com.github.niyaz000.ratehub.service.RatingService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import static com.github.niyaz000.ratehub.constants.ApiConstants.MAX_PER_PAGE;
import static com.github.niyaz000.ratehub.constants.ApiConstants.MIN_PER_PAGE;
import static com.github.niyaz000.ratehub.constants.Routes.RATINGS_ENDPOINT;
import static com.github.niyaz000.ratehub.constants.Routes.SINGLE_RATINGS_ENDPOINT;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class RatingController {

  private final RatingService ratingService;

  @GetMapping(SINGLE_RATINGS_ENDPOINT)
  @PreAuthorize(AppConstants.ALL_ROLES)
  public RatingGetResponse findRatingById(@NotNull @Min (1) @PathVariable(AppConstants.ID) Long id) {
    return ratingService.findById(RequestContext.getTenant(), AuthContext.getUserId(), id);
  }

  @GetMapping(RATINGS_ENDPOINT)
  @PreAuthorize(AppConstants.ALL_ROLES)
  public UserRatingsResponse findAllRatingForUser(@NotBlank @Size(min = 1, max = 64) @RequestParam("user_id") String userId,
                                                  @Min (MIN_PER_PAGE) @Max(MAX_PER_PAGE) @RequestParam(name = "per_page", required = false, defaultValue = "50") int perPage,
                                                  @Min (1) @Max (MAX_PER_PAGE) @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                  @NotNull @RequestParam("sort") Sort.Direction direction) {
    return ratingService.findAllRatingForUser(RequestContext.getTenant(), userId, page, perPage, direction);
  }

  @GetMapping(SINGLE_RATINGS_ENDPOINT)
  @PreAuthorize(AppConstants.SYSTEM_AND_ADMIN)
  public void deleteRatingById(@NotNull @Min (1) @PathVariable(AppConstants.ID) Long id) {
    ratingService.deleteRatingById(RequestContext.getTenant(), id);
  }

}
