package com.sportphotos.infrastructure.database;

import com.sportphotos.domain.events.PhotographersRepository;
import com.sportphotos.domain.events.model.Photographer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotographersRepositorySpringData
    extends PhotographersRepository, MongoRepository<Photographer, String> {}
