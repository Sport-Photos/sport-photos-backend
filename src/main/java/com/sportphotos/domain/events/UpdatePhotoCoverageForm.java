package com.sportphotos.domain.events;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdatePhotoCoverageForm {
  @Schema(example = "Photos from iron man challenge")
  @NotBlank
  private String description;

  @Schema(example = "https://s3.amazonaws.com/vader/iron_man")
  @NotBlank
  private String link;
}
