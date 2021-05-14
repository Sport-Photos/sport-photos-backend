package com.sportphotos.domain.events;

import com.sportphotos.domain.events.model.Event;
import java.util.Optional;

public interface EventsRepository {

  Optional<Event> findById(String eventId);

  Event save(Event event);

  void deleteById(String id);
}
