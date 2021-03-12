package com.sportphotos.fake;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fake Controller")
@Slf4j
@RestController()
@RequestMapping("/fake")
public class FakeController {

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Returns fake object")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public FakeOutput get() {
    log.info("GET /fake called");
    return new FakeOutput(1, "fake");
  }
}
