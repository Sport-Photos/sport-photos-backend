package com.sportphotos.domain.photographers.model;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.ResourceNotFoundException;
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
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");

    Rating rating = getRatingById(ratingId);
    rating.update(updateRatingForm);
    return rating;
  }

  public void deleteRating(String ratingId) {
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");

    ratings.removeIf(rating -> ratingId.equals(rating.getId()));
  }

  public Rating getRatingById(String ratingId) {
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");

    return ratings.stream()
        .filter(rating -> ratingId.equals(rating.getId()))
        .findFirst()
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Rating - [%s] - not found for Photographer - [%s]", ratingId, id)));
  }
}
