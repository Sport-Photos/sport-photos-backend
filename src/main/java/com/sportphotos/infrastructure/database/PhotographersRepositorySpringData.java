package com.sportphotos.infrastructure.database;

import com.sportphotos.domain.photographers.PhotographersRepository;
import com.sportphotos.domain.photographers.model.Photographer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotographersRepositorySpringData
    extends PhotographersRepository, MongoRepository<Photographer, String> {}
