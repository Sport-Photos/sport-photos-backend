package com.sportphotos.domain.events

import com.sportphotos.domain.Clearable
import com.sportphotos.domain.events.model.Photographer

import java.util.concurrent.ConcurrentHashMap

class InMemoryPhotographersRepository implements PhotographersRepository, Clearable {

    final Map<String, Photographer> data = new ConcurrentHashMap<>()

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
