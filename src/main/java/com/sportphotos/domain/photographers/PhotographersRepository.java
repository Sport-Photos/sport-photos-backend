package com.sportphotos.domain.photographers;

import com.sportphotos.domain.photographers.model.Photographer;
import java.util.List;
import java.util.Optional;

public interface PhotographersRepository {

  List<Photographer> findAll();

  Optional<Photographer> findById(String id);

  Optional<Photographer> findByNickname(String nickname);

  Photographer save(Photographer photographer);
}
