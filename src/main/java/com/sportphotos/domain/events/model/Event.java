package com.sportphotos.domain.events.model;

import static com.sportphotos.domain.Preconditions.notNull;
import static com.sportphotos.domain.Preconditions.notNullOrEmpty;
import static com.sportphotos.domain.ResourceNotFoundException.notFoundFor;

import com.sportphotos.domain.events.UpdatePhotoCoverageForm;
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
import org.springframework.web.multipart.MultipartFile;

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
    notNull(photoCoverage, "photoCoverage");

    photoCoverages.add(photoCoverage);
  }

  public PhotoCoverage updatePhotoCoverage(
      String photoCoverageId,
      UpdatePhotoCoverageForm updatePhotoCoverageForm,
      MultipartFile bestPhoto) {
    notNullOrEmpty(photoCoverageId, "photoCoverageId");
    notNull(updatePhotoCoverageForm, "updatePhotoCoverageForm");
    notNull(bestPhoto, "bestPhoto");

    var photoCoverage = getPhotoCoverageById(photoCoverageId);
    photoCoverage.update(updatePhotoCoverageForm, bestPhoto);

    return photoCoverage;
  }

  public PhotoCoverage getPhotoCoverageById(String photoCoverageId) {
    notNullOrEmpty(photoCoverageId, "photoCoverageId");

    return photoCoverages.stream()
        .filter(photoCoverage -> photoCoverageId.equals(photoCoverage.getId()))
        .findFirst()
        .orElseThrow(notFoundFor("Photo Coverage", photoCoverageId, "Event", id));
  }

  public void deletePhotoCoverage(String photoCoverageId) {
    notNullOrEmpty(photoCoverageId, "photoCoverageId");

    photoCoverages.removeIf(photoCoverage -> photoCoverageId.equals(photoCoverage.getId()));
  }
}
