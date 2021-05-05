package com.sportphotos.domain.events;

import com.sportphotos.domain.events.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventsRepository {

  List<Event> findAll();

  Optional<Event> findById(String eventId);

  Event save(Event event);
}
