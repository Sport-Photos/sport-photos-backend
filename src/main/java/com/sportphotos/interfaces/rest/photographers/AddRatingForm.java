package com.sportphotos.interfaces.rest.photographers;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class AddRatingForm {

  @Schema(example = "3")
  @NotNull
  Integer rate;

  @Schema(example = "Average photographer")
  String comment;
}
