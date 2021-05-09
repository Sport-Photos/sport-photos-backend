package com.sportphotos.domain.events;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.events.model.Photographer;
import com.sportphotos.domain.events.model.Rating;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhotographersService {

  private final PhotographersRepository photographersRepository;

  public Rating rate(String photographerId, Rating rating) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "eventId is null or empty");

    Photographer photographer = findById(photographerId);
    photographer.addRating(rating);
    photographersRepository.save(photographer);

    return rating;
  }

  public Photographer findById(String photographerId) {
    checkArgument(!Strings.isNullOrEmpty(photographerId), "eventId is null or empty");

    return photographersRepository
        .findById(photographerId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Photographer - [%s] - not found.", photographerId)));
  }
}
