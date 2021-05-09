package com.sportphotos.domain.photographers.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photographer {

  @Id private String id;

  @Indexed private String nickname;
  private List<Rating> ratings = new ArrayList<>();

  public void addRating(Rating rating) {
    ratings.add(rating);
  }
}
