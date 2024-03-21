package com.github.niyaz000.ratehub.controller;

import com.github.niyaz000.ratehub.auth.AuthContext;
import com.github.niyaz000.ratehub.constants.AppConstants;
import com.github.niyaz000.ratehub.dto.RequestContext;
import com.github.niyaz000.ratehub.request.RatingCreateRequest;
import com.github.niyaz000.ratehub.response.ProductRatingSummaryResponse;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.response.RatingsGetResponse;
import com.github.niyaz000.ratehub.service.RatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static com.github.niyaz000.ratehub.constants.ApiConstants.MAX_PER_PAGE;
import static com.github.niyaz000.ratehub.constants.ApiConstants.MIN_PER_PAGE;
import static com.github.niyaz000.ratehub.constants.AppConstants.*;
import static com.github.niyaz000.ratehub.constants.Routes.*;

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
  public RatingsGetResponse findAllRatingByUser(@NotBlank @Size(min = 1, max = MAX_UUID_LENGTH) @RequestParam("user_id") String userId,
                                                @Min (MIN_PER_PAGE) @Max(MAX_PER_PAGE) @RequestParam(name = "per_page", required = false, defaultValue = "10") int perPage,
                                                @Min (1) @Max (MAX_PER_PAGE) @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                @NotNull @RequestParam(value = "sort", required = false, defaultValue = "DESC") Sort.Direction direction) {
    return ratingService.findAllRatingForUser(RequestContext.getTenant(), userId, page, perPage, direction);
  }

  @GetMapping(PRODUCT_RATINGS_ENDPOINT)
  @PreAuthorize(AppConstants.ALL_ROLES)
  public RatingsGetResponse findAllRatingForProduct(@NotBlank @Size(min = MIN_UUID_LENGTH, max = MAX_UUID_LENGTH) @PathVariable(ID) String productId,
                                                    @Min (MIN_PER_PAGE) @Max(MAX_PER_PAGE) @RequestParam(name = "per_page", required = false, defaultValue = "50") int perPage,
                                                    @Min (1) @Max (MAX_PER_PAGE) @RequestParam(name = "last_record_id", required = false, defaultValue = "0") long lastRecordId) {
    return ratingService.findAllRatingForProduct(RequestContext.getTenant(), productId, perPage, lastRecordId);
  }

  @DeleteMapping(SINGLE_RATINGS_ENDPOINT)
  @PreAuthorize(AppConstants.SYSTEM_AND_ADMIN)
  public void deleteRatingById(@NotNull @Min(MIN_UUID_LENGTH) @PathVariable(AppConstants.ID) Long id) {
    ratingService.deleteRatingById(RequestContext.getTenant(), id);
  }

  @GetMapping(PRODUCT_RATINGS_SUMMARY_ENDPOINT)
  @PreAuthorize(AppConstants.SYSTEM_AND_ADMIN)
  public ProductRatingSummaryResponse getRatingSummary(@NotBlank @Size(min = MIN_UUID_LENGTH, max = MAX_UUID_LENGTH) @PathVariable(ID) String productId) {
    return ratingService.getRatingSummary(RequestContext.getTenant(), productId);
  }

  @PutMapping(PRODUCT_RATINGS_ENDPOINT)
  @PreAuthorize(AppConstants.ALL_ROLES)
  public RatingGetResponse addOrUpdateRating(@NotBlank @Size(min = MIN_UUID_LENGTH, max = MAX_UUID_LENGTH) @PathVariable(ID) String productId,
                                                @Valid @RequestBody RatingCreateRequest request) {
    return ratingService.addOrUpdateRating(RequestContext.getTenant(), request, productId, AuthContext.getUserId());
  }

}
