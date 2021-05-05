package com.sportphotos.domain.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoCoverage {
  private String id;
  @Setter @DBRef private Photographer photographer;
  private String description;
  private String link;
  private Binary bestPhoto;
}
