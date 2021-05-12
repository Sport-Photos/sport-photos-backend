package com.sportphotos.interfaces.rest.events;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.sportphotos.domain.events.AddCoverageForm;
import com.sportphotos.domain.events.AddEventForm;
import com.sportphotos.domain.events.EventsRepository;
import com.sportphotos.domain.events.EventsService;
import com.sportphotos.domain.events.UpdatePhotoCoverageForm;
import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.PhotoCoverage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Tag(name = "Event Controller")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventsController {

  private final EventsRepository repository;
  private final EventsService service;

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Returns all events")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public List<Event> getEvents() {
    return repository.findAll();
  }

  @GetMapping(value = "/{event_id}", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Returns event with given id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public Event getEvent(
      @Parameter(
              description = "Id of Event",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("event_id")
          String eventId) {
    return service.findById(eventId);
  }

  /**
   * Multipart issue explained in
   * https://github.com/springdoc/springdoc-openapi/issues/820#issuecomment-672875450
   */
  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Adds new Event")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Event> addEvent(
      @Parameter(
              description = "Event data",
              required = true,
              schema = @Schema(type = "string", format = "binary"))
          @RequestPart("event_data")
          @Validated
          AddEventForm addEventForm,
      @Parameter(description = "Avatar", required = true) @RequestPart("avatar")
          MultipartFile avatar,
      UriComponentsBuilder b) {

    Event saved = service.addEvent(addEventForm, avatar);

    UriComponents uriComponents = b.path("/api/events/{event_id}").buildAndExpand(saved.getId());

    return ResponseEntity.created(uriComponents.toUri()).body(saved);
  }

  @DeleteMapping(value = "/{event_id}")
  @Operation(summary = "Deletes Event")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Void> deleteEvent(
      @Parameter(description = "Event Id", required = true) @PathVariable("event_id")
          String eventId) {

    service.deleteEvent(eventId);

    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/{event_id}/photo_coverages", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all Photo Coverages for Event")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public List<PhotoCoverage> getPhotoCoverages(
      @Parameter(description = "Event Id", required = true) @PathVariable("event_id")
          String eventId) {

    return service.getAllPhotoCoverages(eventId);
  }

  @GetMapping(
      value = "/{event_id}/photo_coverages/{photo_coverage_id}",
      produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get Photo Coverage for Event")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public PhotoCoverage getPhotoCoverage(
      @Parameter(description = "Event Id", required = true) @PathVariable("event_id")
          String eventId,
      @Parameter(description = "Photo Coverage Id", required = true)
          @PathVariable("photo_coverage_id")
          String photoCoverageId) {

    return service.getPhotoCoverage(eventId, photoCoverageId);
  }

  /**
   * Multipart issue explained in
   * https://github.com/springdoc/springdoc-openapi/issues/820#issuecomment-672875450
   */
  @PostMapping(
      value = "/{event_id}/photo_coverages",
      produces = APPLICATION_JSON_VALUE,
      consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Adds new Photo Coverage")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<PhotoCoverage> addPhotoCoverage(
      @Parameter(
              description = "Coverage date",
              required = true,
              schema = @Schema(type = "string", format = "binary"))
          @RequestPart("coverage_data")
          @Validated
          AddCoverageForm addCoverageForm,
      @Parameter(description = "Best photo", required = true) @RequestPart("best_photo")
          MultipartFile bestPhoto,
      @Parameter(description = "Event Id", required = true) @PathVariable("event_id")
          String eventId,
      UriComponentsBuilder b) {

    PhotoCoverage saved =
        service.addPhotoCoverage(eventId, addCoverageForm.getNick(), addCoverageForm, bestPhoto);

    UriComponents uriComponents =
        b.path("/api/events/{event_id}/photo_coverages/{photo_coverage_id}")
            .buildAndExpand(eventId, saved.getId());

    return ResponseEntity.created(uriComponents.toUri()).body(saved);
  }

  @PatchMapping(
      value = "/{event_id}/photo_coverages/{photo_coverage_id}",
      produces = APPLICATION_JSON_VALUE,
      consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Updates Photo Coverage for Event")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public PhotoCoverage updatePhotoCoverage(
      @Parameter(
              description = "Coverage date",
              required = true,
              schema = @Schema(type = "string", format = "binary"))
          @RequestPart("coverage_data")
          @Validated
          UpdatePhotoCoverageForm updatePhotoCoverageForm,
      @Parameter(description = "Best photo", required = true) @RequestPart("best_photo")
          MultipartFile bestPhoto,
      @Parameter(description = "Event Id", required = true) @PathVariable("event_id")
          String eventId,
      @Parameter(description = "Photo Coverage Id", required = true)
          @PathVariable("photo_coverage_id")
          String photoCoverageId) {

    return service.updatePhotoCoverage(
        eventId, photoCoverageId, updatePhotoCoverageForm, bestPhoto);
  }

  @DeleteMapping(value = "/{event_id}/photo_coverages/{photo_coverage_id}")
  @Operation(summary = "Deletes Photo Coverage frm Event")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Void> deletePhotoCoverage(
      @Parameter(description = "Event Id", required = true) @PathVariable("event_id")
          String eventId,
      @Parameter(description = "Photo Coverage Id", required = true)
          @PathVariable("photo_coverage_id")
          String photoCoverageId) {

    service.deletePhotoCoverage(eventId, photoCoverageId);

    return ResponseEntity.noContent().build();
  }
}
