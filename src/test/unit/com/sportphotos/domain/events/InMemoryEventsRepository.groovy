package com.sportphotos.domain.events

import com.sportphotos.domain.Clearable
import com.sportphotos.domain.events.model.Event

import java.util.concurrent.ConcurrentHashMap

class InMemoryEventsRepository implements EventsRepository, Clearable {

    final Map<String, Event> data = new ConcurrentHashMap<>()

    @Override
    Optional<Event> findById(String eventId) {
        Optional.ofNullable(data.values().find { it.id == eventId })
    }

    @Override
    Event save(Event event) {
        data.put(event.id, event)
        data.get(event.id)
    }

    @Override
    void deleteById(String id) {
        data.remove(id)
    }

    @Override
    void clear() {
        data.clear()
    }
}
