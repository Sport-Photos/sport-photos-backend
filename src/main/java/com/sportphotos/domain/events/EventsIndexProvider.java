package com.sportphotos.domain.events;

import com.sportphotos.domain.events.model.Event;

public interface EventsIndexProvider {

  void addToIndex(Event event);

  void removeFromIndex(String eventId);
}
