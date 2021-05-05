package com.sportphotos.domain

import com.sportphotos.domain.events.EventsService
import com.sportphotos.domain.events.InMemoryEventsRepository
import com.sportphotos.domain.events.InMemoryPhotographersRepository
import com.sportphotos.domain.events.PhotographersService
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
    InMemoryPhotographersRepository photographersRepository

    def setup() {
        eventsRepository.clear()
        photographersRepository.clear()
    }
}
