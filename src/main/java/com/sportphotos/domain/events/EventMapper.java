package com.sportphotos.domain.events;

import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.Location;
import com.sportphotos.domain.events.model.PhotoCoverage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import lombok.SneakyThrows;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class EventMapper {

  /** https://www.baeldung.com/spring-boot-mongodb-upload-file */
  @SneakyThrows(IOException.class)
  Event map(AddEventForm form, MultipartFile avatar) {
    return Event.builder()
        .id(UUID.randomUUID().toString())
        .date(form.getDate())
        .name(form.getName())
        .location(new Location(form.getCity(), form.getCountry()))
        .avatar(new Binary(BsonBinarySubType.BINARY, avatar.getBytes()))
        .photoCoverages(new ArrayList<>())
        .build();
  }

  @SneakyThrows(IOException.class)
  PhotoCoverage map(AddCoverageForm form, MultipartFile bestPhoto) {
    return PhotoCoverage.builder()
        .id(UUID.randomUUID().toString())
        .description(form.getDescription())
        .link(form.getLink())
        .bestPhoto(new Binary(BsonBinarySubType.BINARY, bestPhoto.getBytes()))
        .build();
  }
}
