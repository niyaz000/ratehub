package com.github.niyaz000.ratehub.mapper;

import com.github.niyaz000.ratehub.model.Rating;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.response.UserRatingsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper
public interface RatingMapper {

  @Mapping(target = "id", source = "rating.id")
  @Mapping(target = "productId", source = "rating.productId")
  @Mapping(target = "userId", source = "rating.userId")
  @Mapping(target = "score", source = "rating.score")
  @Mapping(target = "verified", source = "rating.verified")
  @Mapping(target = "weight", source = "rating.weight")
  @Mapping(target = "region", source = "rating.region")
  @Mapping(target = "variation", source = "rating.variation")
  @Mapping(target = "meta", source = "rating.meta")
  @Mapping(target = "tenantName", source = "tenantName")
  RatingGetResponse mapRatingGetRequestToResponse(Rating rating, String tenantName);

  default UserRatingsResponse mapGetUserRatingsToResponse(Page<Rating> ratings, String tenantName) {
    var response = new UserRatingsResponse();
    response.setTotalCount(ratings.getTotalElements());
    var data = ratings
      .stream()
      .map(rating -> mapRatingGetRequestToResponse(rating, tenantName))
      .toList();
    response.setData(data);
    return response;
  }

}
