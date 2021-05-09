package com.sportphotos.domain.photographers;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdateRatingForm {

  @Schema(example = "3")
  @NotNull
  Integer rate;

  @Schema(example = "Average photographer")
  String comment;
}
