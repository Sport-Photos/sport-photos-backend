package com.sportphotos.domain.events.model;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.sportphotos.domain.ResourceNotFoundException;
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
    checkArgument(photoCoverage != null, "photoCoverage is null");

    photoCoverages.add(photoCoverage);
  }

  public PhotoCoverage updatePhotoCoverage(
      String photoCoverageId,
      UpdatePhotoCoverageForm updatePhotoCoverageForm,
      MultipartFile bestPhoto) {
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");
    checkArgument(updatePhotoCoverageForm != null, "updatePhotoCoverageForm is null");
    checkArgument(bestPhoto != null, "bestPhoto is null");

    PhotoCoverage photoCoverage = getPhotoCoverageById(photoCoverageId);
    photoCoverage.update(updatePhotoCoverageForm, bestPhoto);

    return photoCoverage;
  }

  public PhotoCoverage getPhotoCoverageById(String photoCoverageId) {
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");

    return photoCoverages.stream()
        .filter(photoCoverage -> photoCoverageId.equals(photoCoverage.getId()))
        .findFirst()
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Photo Coverage - [%s] - not found for Event - [%s]",
                        photoCoverageId, id)));
  }

  public void deletePhotoCoverage(String photoCoverageId) {
    checkArgument(!Strings.isNullOrEmpty(photoCoverageId), "photoCoverageId is null or empty");

    photoCoverages.removeIf(photoCoverage -> photoCoverageId.equals(photoCoverage.getId()));
  }
}
