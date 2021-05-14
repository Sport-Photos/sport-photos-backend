package com.sportphotos.infrastructure.database


import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import static com.sportphotos.domain.model.EventMock.randomEvent

@MongoTest
class EventsQueryRepositoryTestIT extends Specification {

    @Autowired
    EventsQueryRepository repository

    def 'findEventSummaries should return list of Event summaries'() {
        given:
            def event = randomEvent()
            repository.save(event)
        when:
            def events = repository.findEventSummaries()
        then:
            events.size() == 1
            events.contains(EventSummary.from(event))
    }
}
