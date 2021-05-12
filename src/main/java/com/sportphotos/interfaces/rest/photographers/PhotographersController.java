package com.sportphotos.interfaces.rest.photographers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.sportphotos.domain.photographers.AddRatingForm;
import com.sportphotos.domain.photographers.PhotographersRepository;
import com.sportphotos.domain.photographers.PhotographersService;
import com.sportphotos.domain.photographers.UpdateRatingForm;
import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.domain.photographers.model.Rating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Tag(name = "Photographers Controller")
@RestController
@RequestMapping("/api/photographers")
@RequiredArgsConstructor
public class PhotographersController {

  private final PhotographersService service;
  private final PhotographersRepository photographersRepository;

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all Photographers")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public List<Photographer> getPhotographers() {

    return photographersRepository.findAll();
  }

  @GetMapping(value = "/{photographer_id}", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get Photographer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public Photographer getPhotographer(
      @Parameter(
              description = "Id of Photographer",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("photographer_id")
          String photographerId) {

    return service.findById(photographerId);
  }

  @GetMapping(value = "/{photographer_id}/ratings", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get Ratings for Photographer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public List<Rating> getRatings(
      @Parameter(
              description = "Id of Photographer",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("photographer_id")
          String photographerId) {

    return service.getAllRatings(photographerId);
  }

  @GetMapping(value = "/{photographer_id}/ratings/{rating_id}", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get Rating for Photographer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public Rating getRating(
      @Parameter(
              description = "Id of Photographer",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("photographer_id")
          String photographerId,
      @Parameter(
              description = "Id of Rating",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("rating_id")
          String ratingId) {

    return service.getRating(photographerId, ratingId);
  }

  @PostMapping(
      value = "/{photographer_id}/ratings",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  @Operation(summary = "Adds new rating to Photographer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Rating> addRating(
      @Parameter(description = "Rating data", required = true) @RequestBody @Validated
          AddRatingForm addRatingForm,
      @Parameter(
              description = "Id of Photographer",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("photographer_id")
          String photographerId,
      UriComponentsBuilder b) {

    Rating saved = service.rate(photographerId, addRatingForm);

    UriComponents uriComponents =
        b.path("/api/photographers/{photographer_id}/ratings/{rating_id}")
            .buildAndExpand(photographerId, saved.getId());

    return ResponseEntity.created(uriComponents.toUri()).body(saved);
  }

  @PatchMapping(
      value = "/{photographer_id}/ratings/{rating_id}",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  @Operation(summary = "Updates rating of Photographer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public Rating updateRating(
      @Parameter(description = "Rating data", required = true) @RequestBody @Validated
          UpdateRatingForm updateRatingForm,
      @Parameter(
              description = "Id of Photographer",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("photographer_id")
          String photographerId,
      @Parameter(
              description = "Id of Photographer's Rating",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("rating_id")
          String ratingId) {

    return service.updateRate(photographerId, ratingId, updateRatingForm);
  }

  @DeleteMapping(value = "/{photographer_id}/ratings/{rating_id}")
  @Operation(summary = "Deletes Rating of Photographer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Void> deleteRating(
      @Parameter(
              description = "Id of Photographer",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("photographer_id")
          String photographerId,
      @Parameter(
              description = "Id of Photographer's Rating",
              required = true,
              example = "a882076c-0cec-4427-948d-7a928fdf1ce0")
          @PathVariable("rating_id")
          String ratingId) {

    service.deleteRate(photographerId, ratingId);

    return ResponseEntity.noContent().build();
  }
}
