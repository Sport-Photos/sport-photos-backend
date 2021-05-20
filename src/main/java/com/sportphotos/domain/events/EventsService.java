package com.sportphotos.domain.events;

import static com.sportphotos.domain.Preconditions.notNull;
import static com.sportphotos.domain.Preconditions.notNullOrEmpty;
import static com.sportphotos.domain.ResourceNotFoundException.notFound;

import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.PhotoCoverage;
import com.sportphotos.domain.photographers.PhotographersRepository;
import com.sportphotos.domain.photographers.model.Photographer;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class EventsService {

  private final EventsRepository eventsRepository;
  private final EventMapper eventMapper;
  private final PhotographersRepository photographersRepository;
  private final EventsIndexProvider eventsIndexProvider;

  public Event getEventById(String eventId) {
    notNullOrEmpty(eventId, "eventId");

    return eventsRepository.findById(eventId).orElseThrow(notFound("Event", eventId));
  }

  public Event addEvent(AddEventForm addEventForm, MultipartFile avatar) {
    notNull(addEventForm, "addEventForm");
    notNull(avatar, "avatar");

    var event = eventMapper.map(addEventForm, avatar);
    eventsIndexProvider.addToIndex(event);

    return eventsRepository.save(event);
  }

  public void deleteEvent(String eventId) {
    notNullOrEmpty(eventId, "eventId");

    eventsRepository.deleteById(eventId);
    eventsIndexProvider.removeFromIndex(eventId);
  }

  public PhotoCoverage addPhotoCoverage(
      String eventId, String nickname, AddCoverageForm addCoverageForm, MultipartFile bestPhoto) {
    notNullOrEmpty(eventId, "eventId");
    notNull(addCoverageForm, "addCoverageForm");
    notNull(bestPhoto, "bestPhoto");

    var photographer =
        photographersRepository.findByNickname(nickname).orElse(addPhotographer(nickname));
    var photoCoverage = eventMapper.map(addCoverageForm, bestPhoto, photographer);
    var event = getEventById(eventId);
    event.addPhotoCoverage(photoCoverage);
    eventsRepository.save(event);

    return photoCoverage;
  }

  public List<PhotoCoverage> getAllPhotoCoverages(String eventId) {
    notNullOrEmpty(eventId, "eventId");

    return getEventById(eventId).getPhotoCoverages();
  }

  public PhotoCoverage getPhotoCoverage(String eventId, String photoCoverageId) {
    notNullOrEmpty(eventId, "eventId");
    notNullOrEmpty(photoCoverageId, "photoCoverageId");

    return getEventById(eventId).getPhotoCoverageById(photoCoverageId);
  }

  public void deletePhotoCoverage(String eventId, String photoCoverageId) {
    notNullOrEmpty(eventId, "eventId");
    notNullOrEmpty(photoCoverageId, "photoCoverageId");

    var event = getEventById(eventId);
    event.deletePhotoCoverage(photoCoverageId);

    eventsRepository.save(event);
  }

  private Photographer addPhotographer(String nickname) {
    var photographer =
        Photographer.builder()
            .id(UUID.randomUUID().toString())
            .nickname(nickname)
            .ratings(Collections.emptyList())
            .build();
    return photographersRepository.save(photographer);
  }

  public PhotoCoverage updatePhotoCoverage(
      String eventId,
      String photoCoverageId,
      UpdatePhotoCoverageForm updatePhotoCoverageForm,
      MultipartFile bestPhoto) {
    notNullOrEmpty(eventId, "eventId");
    notNullOrEmpty(photoCoverageId, "photoCoverageId");
    notNull(updatePhotoCoverageForm, "updatePhotoCoverageForm");
    notNull(bestPhoto, "bestPhoto");

    var event = getEventById(eventId);
    var photoCoverage =
        event.updatePhotoCoverage(photoCoverageId, updatePhotoCoverageForm, bestPhoto);
    eventsRepository.save(event);

    return photoCoverage;
  }
}
