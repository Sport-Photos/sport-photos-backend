package com.sportphotos.infrastructure.database;

import com.sportphotos.domain.events.model.Event;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface EventsQueryRepository extends MongoRepository<Event, String> {

  @Query(value = "{}", fields = "{ 'id' : 1, 'date' : 1, 'name': 1, 'location': 1, 'avatar': 1}")
  List<EventSummary> findEventSummaries();
}
