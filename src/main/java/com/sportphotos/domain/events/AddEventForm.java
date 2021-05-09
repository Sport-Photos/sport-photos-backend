package com.sportphotos.domain.events;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AddEventForm {

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
