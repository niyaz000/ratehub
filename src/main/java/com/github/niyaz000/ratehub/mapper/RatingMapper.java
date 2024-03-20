package com.github.niyaz000.ratehub.mapper;

import com.github.niyaz000.ratehub.model.Rating;
import com.github.niyaz000.ratehub.model.RatingSummary;
import com.github.niyaz000.ratehub.request.RatingCreateRequest;
import com.github.niyaz000.ratehub.response.ProductRatingSummaryResponse;
import com.github.niyaz000.ratehub.response.RatingGetResponse;
import com.github.niyaz000.ratehub.response.RatingsGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

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


  @Mapping(target = "score", source = "request.score")
  @Mapping(target = "verified", source = "request.verified")
  @Mapping(target = "region", source = "request.region")
  @Mapping(target = "weight", source = "request.weight")
  @Mapping(target = "variation", source = "request.variation")
  @Mapping(target = "meta", source = "request.meta")
  @Mapping(target = "tenantId", source = "tenantId")
  @Mapping(target = "userId", source = "userId")
  @Mapping(target = "productId", source = "productId")
  Rating mapRatingCreateRequestToRating(RatingCreateRequest request, Long tenantId, String productId, String userId);

  default RatingsGetResponse mapGetRatingsRequestToResponse(Page<Rating> ratings, String tenantName) {
    var response = new RatingsGetResponse();
    response.setTotalCount(ratings.getTotalElements());
    var data = ratings
      .stream()
      .map(rating -> mapRatingGetRequestToResponse(rating, tenantName))
      .toList();
    response.setData(data);
    return response;
  }

  default ProductRatingSummaryResponse mapGetProductRatingSummaryRequestToResponse(RatingSummary ratingSummary,
                                                                                   String tenantName) {
    var response = new ProductRatingSummaryResponse();
    response.setTenantName(tenantName);
    response.setProductId(ratingSummary.getProductId());
    response.setScore(ratingSummary.getScore());
    response.setUpdatedAt(ratingSummary.getUpdatedAt());
    response.setTotalCount(ratingSummary.getTotalCount());
    response.setStarRatings(buildRatings(ratingSummary));
    return response;
  }

  default List<ProductRatingSummaryResponse.StarRating> buildRatings(RatingSummary ratingSummary) {
    List<ProductRatingSummaryResponse.StarRating> ratings = new ArrayList<>();
    long percentage = ratingSummary.getFiveStarCount() / ratingSummary.getTotalCount();
    ratings.add(new ProductRatingSummaryResponse.StarRating(5, ratingSummary.getFiveStarCount(), percentage));

    percentage = ratingSummary.getFourStarCount() / ratingSummary.getTotalCount();
    ratings.add(new ProductRatingSummaryResponse.StarRating(4, ratingSummary.getFourStarCount(), percentage));

    percentage = ratingSummary.getThreeStarCount() / ratingSummary.getTotalCount();
    ratings.add(new ProductRatingSummaryResponse.StarRating(3, ratingSummary.getThreeStarCount(), percentage));

    percentage = ratingSummary.getTwoStarCount() / ratingSummary.getTotalCount();
    ratings.add(new ProductRatingSummaryResponse.StarRating(2, ratingSummary.getTwoStarCount(), percentage));

    percentage = ratingSummary.getFourStarCount() / ratingSummary.getTotalCount();
    ratings.add(new ProductRatingSummaryResponse.StarRating(1, ratingSummary.getOneStarCount(), percentage));
    return ratings;
  }

}
