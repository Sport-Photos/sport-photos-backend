package com.sportphotos.domain.events

import com.sportphotos.domain.Clearable
import com.sportphotos.domain.events.model.Event
import com.sportphotos.infrastructure.algolia.AlgoliaSearchClient

import java.util.concurrent.ConcurrentHashMap

class InMemoryEventsIndexProvider implements EventsIndexProvider, Clearable {

    final Map<String, AlgoliaSearchClient.EventsIndex> data = new ConcurrentHashMap<>()

    @Override
    void addToIndex(Event event) {
        data.put(event.id, AlgoliaSearchClient.EventsIndex.from(event))
    }

    @Override
    void removeFromIndex(String eventId) {
        data.remove(eventId)
    }

    Optional<AlgoliaSearchClient.EventsIndex> findById(String eventId) {
        Optional.ofNullable(data.get(eventId))
    }

    @Override
    void clear() {
        data.clear()
    }
}
