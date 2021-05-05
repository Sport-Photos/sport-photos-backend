package com.sportphotos.domain.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
  private String id;
  private Integer rate;
  private String comment;
}
