package com.sportphotos.domain.events.model;

import com.sportphotos.domain.events.UpdatePhotoCoverageForm;
import com.sportphotos.domain.photographers.model.Photographer;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.web.multipart.MultipartFile;

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

  @SneakyThrows(IOException.class)
  public void update(UpdatePhotoCoverageForm updatePhotoCoverageForm, MultipartFile bestPhoto) {
    this.description = updatePhotoCoverageForm.getDescription();
    this.link = updatePhotoCoverageForm.getLink();
    this.bestPhoto = new Binary(BsonBinarySubType.BINARY, bestPhoto.getBytes());
  }
}
