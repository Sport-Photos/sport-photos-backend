package com.sportphotos.interfaces.rest.events

import com.sportphotos.domain.events.EventsRepository
import com.sportphotos.domain.events.EventsService
import com.sportphotos.domain.events.model.Event
import com.sportphotos.interfaces.rest.ControllerTest
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.sportphotos.domain.model.EventMock.randomEvent
import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.notNullValue
import static org.hamcrest.Matchers.empty

@ControllerTest
class EventsControllerTestIT extends Specification {

    @LocalServerPort
    int port

    @Autowired
    EventsService eventServiceMock

    @Autowired
    EventsRepository eventsRepositoryMock

    def 'GET /api/events should return all events'() {
        given: 'Two events are stored in database'
            def event1 = randomEvent()
            def event2 = randomEvent()
            eventsRepositoryMock.findAll() >> [event1, event2]

        expect: 'Both events are returned'
            given()
                .get(path())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(event1.id))
                .body('[1].id', equalTo(event2.id))
    }

    def 'GET /api/events/{event_id} should return single event'() {
        given: 'Some event is stored in database'
            def eventId = ObjectId.get()
            def event = randomEvent(id: eventId)
            eventServiceMock.findById(eventId.toHexString()) >> event

        expect: 'Requested Event is returned'
            given()
                .get(path(eventId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', notNullValue())
                .body('date', equalTo(event.date.toString()))
                .body('name', equalTo(event.name))
                .body('location.city', equalTo(event.location.city))
                .body('location.country', equalTo(event.location.country))
                .body('avatar', notNullValue())
                .body('photo_coverages', notNullValue())
    }

    def 'POST /api/events should add event'() {
        given: 'Some event, which suppose to be stored in database'
            def eventId = ObjectId.get()
            def event = randomEvent(id: eventId, photoCoverages: [])
            eventServiceMock.save(_ as Event) >> event
        expect: 'Event is saved in database and its body and location is returned'
            given()
                .multiPart("event_data", new File('src/test/resources/file.json'), 'application/json')
                .multiPart('avatar', 'avatar.png', event.getAvatar().getData(), 'image/jpeg')
                .when()
                .post(path())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header('Location', "${path()}/${eventId.toHexString()}")
                .body('id', notNullValue())
                .body('date', equalTo(event.date.toString()))
                .body('name', equalTo(event.name))
                .body('location.city', equalTo(event.location.city))
                .body('location.country', equalTo(event.location.country))
                .body('avatar', notNullValue())
                .body('photo_coverages', empty())
    }

    def path() {
        "http://localhost:$port/api/events"
    }

    def path(def eventId) {
        "${path()}/$eventId"
    }
}
