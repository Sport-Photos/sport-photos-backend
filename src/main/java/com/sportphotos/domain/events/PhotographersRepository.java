package com.sportphotos.domain.events;

import com.sportphotos.domain.events.model.Photographer;
import java.util.Optional;

public interface PhotographersRepository {

  Optional<Photographer> findById(String id);

  Optional<Photographer> findByNickname(String nickname);

  Photographer save(Photographer photographer);
}
