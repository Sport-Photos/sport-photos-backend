package com.sportphotos.interfaces.rest

import com.sportphotos.domain.events.EventsRepository
import com.sportphotos.domain.events.EventsService
import com.sportphotos.domain.events.PhotographersRepository
import com.sportphotos.domain.events.PhotographersService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory

@TestConfiguration
class RestTestConfig {

    def factory = new DetachedMockFactory()

    @Bean
    EventsService eventsService() {
        factory.Mock(EventsService)
    }

    @Bean
    PhotographersService photographersService() {
        factory.Mock(PhotographersService)
    }

    @Bean
    EventsRepository eventsRepository() {
        factory.Mock(EventsRepository)
    }

    @Bean
    PhotographersRepository photographersRepository() {
        factory.Mock(PhotographersRepository)
    }
}
