package com.sportphotos.domain.events;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.PhotoCoverage;
import com.sportphotos.domain.events.model.Photographer;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventsService {

  private final EventsRepository eventsRepository;
  private final PhotographersRepository photographersRepository;

  public Event findById(String eventId) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");

    return eventsRepository
        .findById(eventId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(String.format("Event - [%s] - not found.", eventId)));
  }

  public Event save(Event event) {
    checkArgument(event != null, "event is null");

    return eventsRepository.save(event);
  }

  public PhotoCoverage save(String eventId, String nickname, PhotoCoverage photoCoverage) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");
    checkArgument(photoCoverage != null, "photoCoverage is null");

    Photographer photographer =
        photographersRepository.findByNickname(nickname).orElse(addPhotographer(nickname));
    photoCoverage.setPhotographer(photographer);
    Event event = findById(eventId);
    event.addPhotoCoverage(photoCoverage);
    eventsRepository.save(event);

    return photoCoverage;
  }

  private Photographer addPhotographer(String nickname) {
    Photographer photographer =
        Photographer.builder().id(UUID.randomUUID().toString()).nickname(nickname).build();
    return photographersRepository.save(photographer);
  }
}
