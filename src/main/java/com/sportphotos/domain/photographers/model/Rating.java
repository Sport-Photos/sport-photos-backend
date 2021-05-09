package com.sportphotos.domain.photographers.model;

import com.sportphotos.domain.photographers.UpdateRatingForm;
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

  public void update(UpdateRatingForm updateRatingForm) {
    this.rate = updateRatingForm.getRate();
    this.comment = updateRatingForm.getComment();
  }
}
