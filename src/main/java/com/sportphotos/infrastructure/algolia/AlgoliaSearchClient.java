package com.sportphotos.infrastructure.algolia;

import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.BatchIndexingResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sportphotos.domain.events.EventsIndexProvider;
import com.sportphotos.domain.events.model.Event;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class AlgoliaSearchClient implements EventsIndexProvider {

  private final String eventsIndexName;
  private final SearchClient client;

  public AlgoliaSearchClient(
      @Value("${algolia.index_name}") String eventsIndexName, SearchClient client) {
    this.eventsIndexName = eventsIndexName;
    this.client = client;
  }

  @Override
  public void addToIndex(Event event) {
    SearchIndex<EventsIndex> eventsIndex = client.initIndex(eventsIndexName, EventsIndex.class);
    BatchIndexingResponse responses = eventsIndex.saveObject(EventsIndex.from(event));
    responses
        .getResponses()
        .forEach(response -> log.info("Search index added for event - [{}]", event.getId()));
  }

  @Override
  public void removeFromIndex(String eventId) {
    SearchIndex<EventsIndex> eventsIndex = client.initIndex(eventsIndexName, EventsIndex.class);
    eventsIndex.deleteObject(eventId);
  }

  @lombok.Value
  public static class EventsIndex {

    @JsonProperty("objectID")
    String objectId;

    LocalDate date;
    String name;
    String city;
    String country;

    public static EventsIndex from(Event event) {
      return new EventsIndex(
          event.getId(),
          event.getDate(),
          event.getName(),
          event.getLocation().getCity(),
          event.getLocation().getCountry());
    }
  }
}
