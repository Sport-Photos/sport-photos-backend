package com.sportphotos.domain.events;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.ResourceNotFoundException;
import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.PhotoCoverage;
import com.sportphotos.domain.photographers.PhotographersRepository;
import com.sportphotos.domain.photographers.model.Photographer;
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

  public Event findById(String eventId) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");

    return eventsRepository
        .findById(eventId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(String.format("Event - [%s] - not found.", eventId)));
  }

  public Event addEvent(AddEventForm addEventForm, MultipartFile avatar) {
    checkArgument(addEventForm != null, "addEventForm is null");
    checkArgument(avatar != null, "avatar is null");

    Event event = eventMapper.map(addEventForm, avatar);
    eventsIndexProvider.addToIndex(event);

    return eventsRepository.save(event);
  }

  public void deleteEvent(String eventId) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");
    eventsRepository.deleteById(eventId);
    eventsIndexProvider.removeFromIndex(eventId);
  }

  public PhotoCoverage addPhotoCoverage(
      String eventId, String nickname, AddCoverageForm addCoverageForm, MultipartFile bestPhoto) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");
    checkArgument(addCoverageForm != null, "addCoverageForm is null");
    checkArgument(bestPhoto != null, "bestPhoto is null");

    Photographer photographer =
        photographersRepository.findByNickname(nickname).orElse(addPhotographer(nickname));
    PhotoCoverage photoCoverage = eventMapper.map(addCoverageForm, bestPhoto);
    photoCoverage.setPhotographer(photographer);
    Event event = findById(eventId);
    event.addPhotoCoverage(photoCoverage);
    eventsRepository.save(event);

    return photoCoverage;
  }

  public List<PhotoCoverage> getAllPhotoCoverages(String eventId) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");

    Event event = findById(eventId);

    return event.getPhotoCoverages();
  }

  public PhotoCoverage getPhotoCoverage(String eventId, String photoCoverageId) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");

    Event event = findById(eventId);

    return findPhotoCoverageById(event, photoCoverageId);
  }

  public void deletePhotoCoverage(String eventId, String photoCoverageId) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");

    Event event = findById(eventId);
    event
        .getPhotoCoverages()
        .removeIf(photoCoverage -> photoCoverageId.equals(photoCoverage.getId()));

    eventsRepository.save(event);
  }

  private Photographer addPhotographer(String nickname) {
    Photographer photographer =
        Photographer.builder().id(UUID.randomUUID().toString()).nickname(nickname).build();
    return photographersRepository.save(photographer);
  }

  private PhotoCoverage findPhotoCoverageById(Event event, String photoCoverageId) {
    checkArgument(event != null, "event is null");
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");

    return event.getPhotoCoverages().stream()
        .filter(photoCoverage -> photoCoverageId.equals(photoCoverage.getId()))
        .findFirst()
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Photo Coverage - [%s] - not found for Event - [%s]",
                        photoCoverageId, event.getId())));
  }

  public PhotoCoverage updatePhotoCoverage(
      String eventId,
      String photoCoverageId,
      UpdatePhotoCoverageForm updatePhotoCoverageForm,
      MultipartFile bestPhoto) {
    checkArgument(!Strings.isNullOrEmpty(eventId), "eventId is null or empty");
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");
    checkArgument(updatePhotoCoverageForm != null, "updatePhotoCoverageForm is null");
    checkArgument(bestPhoto != null, "bestPhoto is null");

    Event event = findById(eventId);
    PhotoCoverage photoCoverage = findPhotoCoverageById(event, photoCoverageId);
    photoCoverage.update(updatePhotoCoverageForm, bestPhoto);
    eventsRepository.save(event);

    return photoCoverage;
  }
}
