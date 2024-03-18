package com.github.niyaz000.ratehub.service;

import com.github.niyaz000.ratehub.dao.RatingDao;
import com.github.niyaz000.ratehub.exception.NotFoundException;
import com.github.niyaz000.ratehub.mapper.RatingMapper;
import com.github.niyaz000.ratehub.model.Tenant;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.response.UserRatingsResponse;
import com.github.niyaz000.ratehub.util.ExceptionUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.github.niyaz000.ratehub.constants.AppConstants.ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

  private final RatingDao ratingDao;

  private final RatingMapper ratingMapper;

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

  public UserRatingsResponse findAllRatingForUser(Tenant tenant,
                                                  String userId,
                                                  int page,
                                                  int perPage,
                                                  Sort.@NotNull Direction direction) {
    var ratings = ratingDao.findAllByTenantIdAndUserId(tenant.getId(), userId, PageRequest.of(page - 1, perPage, direction));
    return ratingMapper.mapGetUserRatingsToResponse(ratings, tenant.getName());
  }

  public void deleteRatingById(Tenant tenant, Long id) {
    ratingDao.deleteByTenantIdAndId(tenant.getId(), id);
  }

}
