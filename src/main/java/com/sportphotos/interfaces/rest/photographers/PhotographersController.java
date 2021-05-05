package com.sportphotos.interfaces.rest.photographers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.sportphotos.domain.events.PhotographersService;
import com.sportphotos.domain.events.model.Rating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
  private final PhotographersMapper mapper;

  /**
   * Multipart issue explained in
   * https://github.com/springdoc/springdoc-openapi/issues/820#issuecomment-672875450
   */
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

    Rating saved = service.rate(photographerId, mapper.map(addRatingForm));

    UriComponents uriComponents =
        b.path("/api/photographers/{photographer_id}/ratings/{rating_id}")
            .buildAndExpand(photographerId, saved.getId());

    return ResponseEntity.created(uriComponents.toUri()).body(saved);
  }
}
