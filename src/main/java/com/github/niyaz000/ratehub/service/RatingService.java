package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dao.RatingDao;
import com.github.niyaz000.ratehub.dao.RatingSummaryDao;
import com.github.niyaz000.ratehub.exception.NotFoundException;
import com.github.niyaz000.ratehub.mapper.RatingMapper;
import com.github.niyaz000.ratehub.model.Rating;
import com.github.niyaz000.ratehub.model.RatingSummary;
import com.github.niyaz000.ratehub.model.Tenant;
import com.github.niyaz000.ratehub.request.RatingCreateRequest;
import com.github.niyaz000.ratehub.response.ProductRatingSummaryResponse;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.response.RatingsGetResponse;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.github.niyaz000.ratehub.constants.AppConstants.ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

  private final RatingDao ratingDao;

  private final RatingMapper ratingMapper;

  private final RatingSummaryDao ratingSummaryDao;

  private final RatingPublisherService publisherService;

  public RatingGetResponse findById(Tenant tenant,
                                    String userId,
                                    Long id) {
    log.info("trying to fetch rating for tenantName {} userId {} rating id {}", tenant.getName(), userId, id);
    if (Objects.isNull(userId)) {
      var rating = ratingDao.findByTenantIdAndId(tenant.getId(), id)
        .orElseThrow(() -> new NotFoundException(ID, id.toString()));
      return ratingMapper.mapRatingGetRequestToResponse(rating, tenant.getName());
    } else {
      var rating = ratingDao.findByTenantIdAndIdAndUserId(tenant.getId(), id, userId)
        .orElseThrow(() -> new NotFoundException(ID, id.toString()));
      if (!rating.getUserId().equals(userId)) {
        log.warn("userId {} does not matching rating resource owner userId {}, forbidden access", userId, rating.getUserId());
        throw ExceptionUtil.unauthorizedException();
      }
      return ratingMapper.mapRatingGetRequestToResponse(rating, tenant.getName());
    }
  }

  public RatingsGetResponse findAllRatingForUser(Tenant tenant,
                                                 String userId,
                                                 int page,
                                                 int perPage,
                                                 Sort.@NotNull Direction direction) {
    var ratings = ratingDao.findAllByTenantIdAndUserId(tenant.getId(), userId, PageRequest.of(page - 1, perPage, direction));
    return ratingMapper.mapGetRatingsRequestToResponse(ratings, tenant.getName());
  }

  public void deleteRatingById(Tenant tenant, Long id) {
    ratingDao.deleteByTenantIdAndId(tenant.getId(), id);
  }

  public RatingsGetResponse findAllRatingForProduct(Tenant tenant,
                                                    String productId,
                                                    int perPage,
                                                    long lastRecordId) {
    var ratings = ratingDao.findAllRatingForProduct(tenant.getId(), productId, lastRecordId, PageRequest.of(0, perPage, Sort.Direction.DESC));
    return ratingMapper.mapGetRatingsRequestToResponse(ratings, tenant.getName());
  }

  public ProductRatingSummaryResponse getRatingSummary(Tenant tenant, String productId) {
    var summary = ratingSummaryDao.findByTenantIdAndProductId(tenant.getId(), productId)
      .orElseThrow();
    return ratingMapper.mapGetProductRatingSummaryRequestToResponse(summary, tenant.getName());
  }

  public RatingGetResponse addOrUpdateRating(Tenant tenant,
                                             RatingCreateRequest request,
                                             String productId,
                                             String userId) {
    ratingSummaryDao.findByTenantIdAndProductId(tenant.getId(), productId)
      .orElseGet(() -> {
        try {
          return ratingSummaryDao.save(RatingSummary.defaultRatingSummary(tenant.getId(), productId));
        } catch (DataIntegrityViolationException ex) {
          return ratingSummaryDao.findByTenantIdAndProductId(tenant.getId(), productId).get();
        }
      });

    Rating rating = createOrUpdateRating(tenant, request, productId, userId);
    return ratingMapper.mapRatingGetRequestToResponse(rating, tenant.getName());
  }

  /* Handle concurrency */
  private Rating createOrUpdateRating(Tenant tenant,
                                      RatingCreateRequest request,
                                      String productId,
                                      String userId) {
    Optional<Rating> record = ratingDao.findByTenantIdAndUserIdAndProductId(tenant.getId(), userId, productId);
    Rating rating;
    if (record.isEmpty()) {
      rating = ratingMapper.mapRatingCreateRequestToRating(request, tenant.getId(), productId, userId);
    } else {
      rating = ratingMapper.mapRatingUpdateRequestToRating(request, record.get());
    }
    publisherService.publishEvent(tenant.getName(), rating);
    rating = ratingDao.save(rating);
    return rating;
  }

}
