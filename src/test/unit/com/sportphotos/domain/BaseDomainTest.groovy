package com.sportphotos.domain

import com.sportphotos.domain.events.EventsService
import com.sportphotos.domain.events.InMemoryEventsIndexProvider
import com.sportphotos.domain.events.InMemoryEventsRepository
import com.sportphotos.domain.photographers.InMemoryPhotographersRepository
import com.sportphotos.domain.photographers.PhotographersService
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

abstract class BaseDomainTest extends Specification {

    @Autowired
    EventsService eventService

    @Autowired
    PhotographersService photographersService

    @Autowired
    InMemoryEventsRepository eventsRepository

    @Autowired
    InMemoryEventsIndexProvider eventsIndexProvider;

    @Autowired
    InMemoryPhotographersRepository photographersRepository

    def setup() {
        eventsRepository.clear()
        photographersRepository.clear()
    }
}
