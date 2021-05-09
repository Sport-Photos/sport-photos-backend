package com.sportphotos.infrastructure.database;

import com.sportphotos.domain.events.EventsRepository;
import com.sportphotos.domain.events.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventsRepositorySpringData
    extends EventsRepository, MongoRepository<Event, String> {}
