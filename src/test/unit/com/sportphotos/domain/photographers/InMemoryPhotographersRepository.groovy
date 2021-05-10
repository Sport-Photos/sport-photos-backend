package com.sportphotos.domain.photographers

import com.sportphotos.domain.Clearable
import com.sportphotos.domain.photographers.model.Photographer

import java.util.concurrent.ConcurrentHashMap

class InMemoryPhotographersRepository implements PhotographersRepository, Clearable {

    final Map<String, Photographer> data = new ConcurrentHashMap<>()

    @Override
    List<Photographer> findAll() {
        new ArrayList<>(data.values())
    }

    @Override
    Optional<Photographer> findById(String id) {
        Optional.ofNullable(data.values().find { it.id == id })
    }

    @Override
    Optional<Photographer> findByNickname(String nickname) {
        Optional.ofNullable(data.values().find { it.nickname == nickname })
    }

    @Override
    Photographer save(Photographer photographer) {
        data.put(photographer.id, photographer)
        data.get(photographer.id)
    }

    @Override
    void clear() {
        data.clear()
    }
}
