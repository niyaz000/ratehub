package com.github.niyaz000.ratehub.repository;

import com.github.niyaz000.ratehub.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

}
