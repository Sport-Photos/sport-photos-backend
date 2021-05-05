package com.sportphotos.domain.events

import com.github.javafaker.Faker
import com.sportphotos.domain.BaseDomainTest
import com.sportphotos.domain.DomainTest
import com.sportphotos.domain.model.PhotoCoverageMock

import static com.sportphotos.domain.model.EventMock.randomEvent
import static com.sportphotos.domain.model.PhotoCoverageMock.randomPhotoCoverage

@DomainTest
class EventsServiceTest extends BaseDomainTest {

    static def random = new Random(1)
    static def faker = new Faker(random)

    def 'findById should throw ResourceNotFoundException when Event not found by given id'() {
        when:
            eventService.findById(UUID.randomUUID().toString())
        then:
            thrown(ResourceNotFoundException)
    }

    def 'save(Event) should store Event in database'() {
        given:
            def event = randomEvent()
        when:
            eventService.save(event)
        then:
            !eventsRepository.findById(event.id).isEmpty()
    }
}
