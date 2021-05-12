package com.sportphotos.interfaces.rest.events

import com.sportphotos.domain.events.AddEventForm
import com.sportphotos.domain.events.EventsRepository
import com.sportphotos.domain.events.EventsService
import com.sportphotos.interfaces.rest.ControllerTest
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

import static com.sportphotos.domain.model.EventMock.randomEvent
import static com.sportphotos.domain.model.PhotoCoverageMock.randomPhotoCoverage
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
            eventServiceMock.getEventById(eventId.toHexString()) >> event

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
            def event = randomEvent(photoCoverages: [])
            eventServiceMock.addEvent(_ as AddEventForm, _ as MultipartFile) >> event
        expect: 'Event is saved in database and its body and location is returned'
            given()
                .multiPart("event_data", new File('src/test/resources/event.json'), 'application/json')
                .multiPart('avatar', 'avatar.png', event.getAvatar().getData(), 'image/jpeg')
                .when()
                .post(path())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header('Location', "${path(event.id)}")
                .body('id', equalTo(event.id))
                .body('date', equalTo(event.date.toString()))
                .body('name', equalTo(event.name))
                .body('location.city', equalTo(event.location.city))
                .body('location.country', equalTo(event.location.country))
                .body('avatar', notNullValue())
                .body('photo_coverages', empty())
    }

    def 'DELETE /api/events/{event_id} should remove event'() {
        given:
            def eventId = UUID.randomUUID().toString()
        expect:
            given()
                .when()
                .delete(path(eventId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
    }

    def 'GET /api/events/{event_id}/photo_coverage should get all photo coverages for event'() {
        given:
            def event = randomEvent()

            eventServiceMock.getAllPhotoCoverages(event.id) >> event.getPhotoCoverages()
        expect:
            given()
                .get(photoCoveragePath(event.id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(event.photoCoverages[0].id))
                .body('[1].id', equalTo(event.photoCoverages[1].id))
    }

    def 'GET /api/events/{event_id}/photo_coverage/{photo_coverage_id} should get photo coverages by given id, for event'() {
        given:
            def event = randomEvent()

            eventServiceMock.getPhotoCoverage(event.id, event.photoCoverages[0].id) >> event.photoCoverages[0]
        expect:
            given()
                .get(photoCoveragePath(event.id, event.photoCoverages[0].id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(event.photoCoverages[0].id))
                .body('photographer.nickname', equalTo(event.photoCoverages[0].photographer.nickname))
                .body('description', equalTo(event.photoCoverages[0].description))
                .body('link', equalTo(event.photoCoverages[0].link))
                .body('best_photo', notNullValue())
    }

    def 'POST /api/events/{event_id}/photo_coverage should add new photo coverage to event'() {
        given: 'Some event, which suppose to be stored in database'
            def event = randomEvent(photoCoverages: [])
            def photoCoverage = randomPhotoCoverage()
            eventServiceMock.addPhotoCoverage(*_) >> photoCoverage
        expect: 'Event is saved in database and its body and location is returned'
            given()
                .multiPart("coverage_data", new File('src/test/resources/coverage.json'), 'application/json')
                .multiPart('best_photo', 'best_photo.png', photoCoverage.getBestPhoto().getData(), 'image/jpeg')
                .when()
                .post(photoCoveragePath(event.id))
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header('Location', "${photoCoveragePath(event.id, photoCoverage.id)}")
                .body('id', equalTo(photoCoverage.id))
                .body('description', equalTo(photoCoverage.description))
                .body('link', equalTo(photoCoverage.link))
                .body('best_photo', notNullValue())
                .body('photographer.nickname', equalTo(photoCoverage.photographer.nickname))
    }

    def 'PATCH /api/events/{event_id}/photo_coverage/{photo_coverage_id}  should update photo coverage'() {
        given: 'Some event, which suppose to be stored in database'
            def event = randomEvent(photoCoverages: [])
            def photoCoverage = randomPhotoCoverage()
            eventServiceMock.updatePhotoCoverage(*_) >> photoCoverage
        expect: 'Photo coverage is updated in database'
            given()
                .multiPart("coverage_data", new File('src/test/resources/update_coverage.json'), 'application/json')
                .multiPart('best_photo', 'best_photo.png', photoCoverage.getBestPhoto().getData(), 'image/jpeg')
                .when()
                .patch(photoCoveragePath(event.id, photoCoverage.id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photoCoverage.id))
                .body('description', equalTo(photoCoverage.description))
                .body('link', equalTo(photoCoverage.link))
                .body('best_photo', notNullValue())
                .body('photographer.nickname', equalTo(photoCoverage.photographer.nickname))
    }

    def 'DELETE /api/events/{event_id}/photo_coverage/{photo_coverage_id} should remove photo coverage from event'() {
        given: 'Some event, which suppose to be stored in database'
            def eventId = UUID.randomUUID().toString()
            def photoCoverageId = UUID.randomUUID().toString()
        expect: 'Event is saved in database and its body and location is returned'
            given()
                .when()
                .delete(photoCoveragePath(eventId, photoCoverageId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
    }

    def path() {
        "http://localhost:$port/api/events"
    }

    def path(def eventId) {
        "${path()}/$eventId"
    }

    def photoCoveragePath(String eventId) {
        "${path(eventId)}/photo_coverages"
    }

    def photoCoveragePath(String eventId, String photoCoverageId) {
        "${photoCoveragePath(eventId)}/$photoCoverageId"
    }
}
