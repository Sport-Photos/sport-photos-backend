package com.sportphotos.infrastructure.database;

import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.Location;
import java.time.LocalDate;
import lombok.Value;
import org.bson.types.Binary;

@Value
public class EventSummary {
  String id;
  LocalDate date;
  String name;
  Location location;
  Binary avatar;

  public static EventSummary from(Event event) {
    return new EventSummary(
        event.getId(), event.getDate(), event.getName(), event.getLocation(), event.getAvatar());
  }
}
