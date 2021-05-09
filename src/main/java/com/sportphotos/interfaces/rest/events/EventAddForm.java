package com.sportphotos.interfaces.rest.events;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class EventAddForm {

  @Schema(example = "2017-04-12")
  @NotNull
  LocalDate date;

  @Schema(example = "Iron Man Challenge")
  @NotBlank
  String name;

  @Schema(example = "Warsaw")
  @NotBlank
  String city;

  @Schema(example = "Poland")
  @NotBlank
  String country;
}
