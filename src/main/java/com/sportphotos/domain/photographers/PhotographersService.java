package com.sportphotos.domain.photographers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.ResourceNotFoundException;
import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.domain.photographers.model.Rating;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhotographersService {

  private final PhotographersRepository photographersRepository;
  private final PhotographersMapper photographersMapper;

  public List<Rating> getAllRatings(String photographerId) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");

    Photographer photographer = getPhotographerById(photographerId);

    return photographer.getRatings();
  }

  public Rating getRating(String photographerId, String ratingId) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");

    return getPhotographerById(photographerId).getRatingById(ratingId);
  }

  public Rating addRating(String photographerId, AddRatingForm addRatingForm) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");
    checkArgument(addRatingForm != null, "addRatingForm is null");

    Photographer photographer = getPhotographerById(photographerId);
    Rating rating = photographersMapper.map(addRatingForm);
    photographer.addRating(rating);
    photographersRepository.save(photographer);

    return rating;
  }

  public Photographer getPhotographerById(String photographerId) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");

    return photographersRepository
        .findById(photographerId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Photographer - [%s] - not found.", photographerId)));
  }

  public Rating updateRating(
      String photographerId, String ratingId, UpdateRatingForm updateRatingForm) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");
    checkArgument(updateRatingForm != null, "updateRatingForm is null");

    Photographer photographer = getPhotographerById(photographerId);
    Rating rating = getPhotographerById(photographerId).updateRating(ratingId, updateRatingForm);
    photographersRepository.save(photographer);

    return rating;
  }

  public void deleteRating(String photographerId, String ratingId) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "photographerId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(ratingId), "ratingId is null or empty");

    Photographer photographer = getPhotographerById(photographerId);
    photographer.deleteRating(ratingId);

    photographersRepository.save(photographer);
  }
}
