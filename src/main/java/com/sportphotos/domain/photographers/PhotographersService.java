package com.sportphotos.domain.photographers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.ResourceNotFoundException;
import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.domain.photographers.model.Rating;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhotographersService {

  private final PhotographersRepository photographersRepository;
  private final PhotographersMapper photographersMapper;

  public Rating rate(String photographerId, AddRatingForm addRatingForm) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");
    checkArgument(addRatingForm != null, "addRatingForm is null");

    Photographer photographer = findById(photographerId);
    Rating rating = photographersMapper.map(addRatingForm);
    photographer.addRating(rating);
    photographersRepository.save(photographer);

    return rating;
  }

  public Photographer findById(String photographerId) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");

    return photographersRepository
        .findById(photographerId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Photographer - [%s] - not found.", photographerId)));
  }

  public Rating updateRate(
      String photographerId, String ratingId, UpdateRatingForm updateRatingForm) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");
    checkArgument(updateRatingForm != null, "updateRatingForm is null");

    Photographer photographer = findById(photographerId);
    Rating rating = findRatingById(photographer, ratingId);
    rating.update(updateRatingForm);
    photographersRepository.save(photographer);

    return rating;
  }

  private Rating findRatingById(Photographer photographer, String ratingId) {
    checkArgument(photographer != null, "photographer is null");
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");

    return photographer.getRatings().stream()
        .filter(rating -> ratingId.equals(rating.getId()))
        .findFirst()
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Rating - [%s] - not found for Photographer - [%s]",
                        ratingId, photographer.getId())));
  }
}
