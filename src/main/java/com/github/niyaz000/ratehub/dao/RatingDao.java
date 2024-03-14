package com.github.niyaz000.ratehub.dao;

import com.github.niyaz000.ratehub.model.Rating;
import com.github.niyaz000.ratehub.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingDao {

  private final RatingRepository repository;

  public Rating save(Rating rating) {
    return repository.save(rating);
  }

}
