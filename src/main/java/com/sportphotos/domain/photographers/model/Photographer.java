package com.sportphotos.domain.photographers.model;

import static com.sportphotos.domain.Preconditions.notNullOrEmpty;
import static com.sportphotos.domain.ResourceNotFoundException.notFoundFor;

import com.sportphotos.domain.photographers.UpdateRatingForm;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photographer {

  @Id private String id;

  @Indexed private String nickname;
  private List<Rating> ratings = new ArrayList<>();

  public void addRating(Rating rating) {
    ratings.add(rating);
  }

  public Rating updateRating(String ratingId, UpdateRatingForm updateRatingForm) {
    notNullOrEmpty(ratingId, "ratingId");

    var rating = getRatingById(ratingId);
    rating.update(updateRatingForm);

    return rating;
  }

  public void deleteRating(String ratingId) {
    notNullOrEmpty(ratingId, "ratingId");

    ratings.removeIf(rating -> ratingId.equals(rating.getId()));
  }

  public Rating getRatingById(String ratingId) {
    notNullOrEmpty(ratingId, "ratingId");

    return ratings.stream()
        .filter(rating -> ratingId.equals(rating.getId()))
        .findFirst()
        .orElseThrow(notFoundFor("Rating", ratingId, "Photographer", id));
  }
}
