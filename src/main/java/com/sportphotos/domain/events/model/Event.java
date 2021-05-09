package com.sportphotos.domain.events.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

  @Id private String id;

  @Indexed private LocalDate date;
  private String name;
  private Location location;
  private List<PhotoCoverage> photoCoverages = new ArrayList<>();
  private Binary avatar;

  public void addPhotoCoverage(PhotoCoverage photoCoverage) {
    photoCoverages.add(photoCoverage);
  }
}
