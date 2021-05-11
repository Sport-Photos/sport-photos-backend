package com.sportphotos.infrastructure.database.init;

import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.infrastructure.algolia.AlgoliaSearchClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!test")
public class DatabaseInitializer {

  private final MongoTemplate mongoTemplate;
  private final AlgoliaSearchClient searchClient;

  @EventListener(ApplicationStartedEvent.class)
  public void onApplicationEvent(ApplicationStartedEvent event) {
    if (!isInitiated()) {
      List<Photographer> photographers = Fakes.fakePhotographers(50, 10);
      List<Event> events = Fakes.fakeEvents(100, 6, photographers);

      mongoTemplate.insertAll(photographers);
      mongoTemplate.insertAll(events);
      events.forEach(searchClient::addToIndex);
    }
  }

  private boolean isInitiated() {
    return mongoTemplate.collectionExists(Event.class)
        || mongoTemplate.collectionExists(Photographer.class);
  }
}
