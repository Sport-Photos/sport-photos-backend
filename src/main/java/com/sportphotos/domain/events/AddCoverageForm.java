package com.sportphotos.domain.events;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AddCoverageForm {
  @Schema(example = "Vader")
  @NotBlank
  String nick;

  @Schema(example = "Photos from iron man challenge")
  @NotBlank
  String description;

  @Schema(example = "https://s3.amazonaws.com/vader/iron_man")
  @NotBlank
  String link;
}
