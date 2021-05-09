package com.sportphotos.interfaces.rest

import com.sportphotos.domain.events.EventMapper
import com.sportphotos.domain.events.EventsRepository
import com.sportphotos.domain.events.EventsService
import com.sportphotos.domain.photographers.PhotographersMapper
import com.sportphotos.domain.photographers.PhotographersRepository
import com.sportphotos.domain.photographers.PhotographersService
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
    EventMapper eventMapper() {
        factory.Mock(EventMapper)
    }

    @Bean
    PhotographersService photographersService() {
        factory.Mock(PhotographersService)
    }

    @Bean
    PhotographersMapper photographersMapper() {
        factory.Mock(PhotographersMapper)
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
