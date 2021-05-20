package com.sportphotos.domain.photographers;

import static com.sportphotos.domain.Preconditions.notNull;
import static com.sportphotos.domain.Preconditions.notNullOrEmpty;
import static com.sportphotos.domain.ResourceNotFoundException.notFound;

import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.domain.photographers.model.Rating;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhotographersService {

  private final PhotographersRepository photographersRepository;
  private final PhotographersMapper photographersMapper;

  public List<Rating> getAllRatings(String photographerId) {
    notNullOrEmpty(photographerId, "photographerId");

    var photographer = getPhotographerById(photographerId);

    return photographer.getRatings();
  }

  public Rating getRating(String photographerId, String ratingId) {
    notNullOrEmpty(photographerId, "photographerId");
    notNullOrEmpty(ratingId, "ratingId");

    return getPhotographerById(photographerId).getRatingById(ratingId);
  }

  public Rating addRating(String photographerId, AddRatingForm addRatingForm) {
    notNullOrEmpty(photographerId, "photographerId");
    notNull(addRatingForm, "addRatingForm");

    var photographer = getPhotographerById(photographerId);
    var rating = photographersMapper.map(addRatingForm);
    photographer.addRating(rating);
    photographersRepository.save(photographer);

    return rating;
  }

  public Photographer getPhotographerById(String photographerId) {
    notNullOrEmpty(photographerId, "photographerId");

    return photographersRepository
        .findById(photographerId)
        .orElseThrow(notFound("Photographer", photographerId));
  }

  public Rating updateRating(
      String photographerId, String ratingId, UpdateRatingForm updateRatingForm) {
    notNullOrEmpty(photographerId, "photographerId");
    notNullOrEmpty(ratingId, "ratingId");
    notNull(updateRatingForm, "updateRatingForm");

    var photographer = getPhotographerById(photographerId);
    var rating = photographer.updateRating(ratingId, updateRatingForm);
    photographersRepository.save(photographer);

    return rating;
  }

  public void deleteRating(String photographerId, String ratingId) {
    notNullOrEmpty(photographerId, "photographerId");
    notNullOrEmpty(ratingId, "ratingId");

    var photographer = getPhotographerById(photographerId);
    photographer.deleteRating(ratingId);

    photographersRepository.save(photographer);
  }
}
