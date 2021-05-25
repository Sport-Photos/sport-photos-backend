package spec

import com.sportphotos.integration.MongoTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.sportphotos.domain.model.AddCoverageFormMock.randomAddCoverageForm
import static com.sportphotos.domain.model.AddEventFormMock.randomAddEventForm
import static com.sportphotos.domain.model.UpdatePhotoCoverageFormMock.randomUpdatePhotoCoverageForm
import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.notNullValue
import static org.hamcrest.Matchers.empty
import static spec.FunctionalTestHelper.getIdFromLocationHeader
import static spec.FunctionalTestHelper.getIdFromResponseBody

@FunctionalTest
class EventsFT extends Specification {

    @Autowired
    FunctionalTestHelper ftHelper

    @Autowired
    MongoTestHelper mongoHelper

    def setup() {
        mongoHelper.cleanDatabase()
    }

    def 'GET /api/events should return list of event summaries stored in database'() {
        given: 'Create two events in database'
            def addEventForm1 = randomAddEventForm()
            def savedEventId1 = ftHelper.createEvent(addEventForm1, [] as byte[])
            def addEventForm2 = randomAddEventForm()
            def savedEventId2 = ftHelper.createEvent(addEventForm2, [] as byte[])
        expect: 'Both events are found in database'
            given()
                .get(ftHelper.path())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(savedEventId1))
                .body('[0].date', equalTo(addEventForm1.date.toString()))
                .body('[0].name', equalTo(addEventForm1.name))
                .body('[0].location.city', equalTo(addEventForm1.city))
                .body('[0].location.country', equalTo(addEventForm1.country))
                .body('[0].avatar', notNullValue())
                .body('[1].id', equalTo(savedEventId2))
                .body('[1].date', equalTo(addEventForm2.date.toString()))
                .body('[1].name', equalTo(addEventForm2.name))
                .body('[1].location.city', equalTo(addEventForm2.city))
                .body('[1].location.country', equalTo(addEventForm2.country))
                .body('[1].avatar', notNullValue())
    }

    def 'GET /api/events/{event_id} should return event with given id'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        expect: 'Event with given id is found'
            given()
                .get(ftHelper.path(savedEventId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(savedEventId))
                .body('date', equalTo(addEventForm.date.toString()))
                .body('name', equalTo(addEventForm.name))
                .body('location.city', equalTo(addEventForm.city))
                .body('location.country', equalTo(addEventForm.country))
                .body('avatar', notNullValue())
    }

    def 'POST /api/events should add event'() {
        given: 'Create add event form'
            def addEventForm = randomAddEventForm()
        when: 'Save event in database'
            def response = given()
                .multiPart("event_data", addEventForm, 'application/json')
                .multiPart('avatar', 'avatar.jpeg', [] as byte[], 'image/jpeg')
                .when()
                .post(ftHelper.path())
                .andReturn()
        then: 'Saved event is returned'
            response.then()
                .statusCode(HttpStatus.CREATED.value())
                .header('Location', notNullValue())
                .body('id', notNullValue())
                .body('date', equalTo(addEventForm.date.toString()))
                .body('name', equalTo(addEventForm.name))
                .body('location.city', equalTo(addEventForm.city))
                .body('location.country', equalTo(addEventForm.country))
                .body('avatar', notNullValue())
                .body('photo_coverages', empty())
        and: 'EventId is returned in Location header'
            def eventIdFromLocationHeader = getIdFromLocationHeader(response)
            def eventIdFromResponseBody = getIdFromResponseBody(response)
            eventIdFromLocationHeader == eventIdFromResponseBody
    }

    def 'DELETE /api/events/{event_id} should remove event'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        when: 'Remove event from database'
            given()
                .when()
                .delete(ftHelper.path(savedEventId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        then: 'Event is no longer stored in database'
            given()
                .get(ftHelper.path(savedEventId))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
    }

    def 'GET /api/events/{event_id}/photo_coverages should get all photo coverages for event'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create two photo coverages for event'
            def addCoverageForm1 = randomAddCoverageForm()
            def photoCoverageId1 = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm1, [] as byte[])
            def addCoverageForm2 = randomAddCoverageForm()
            def photoCoverageId2 = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm2, [] as byte[])
        expect: 'Both photo coverages are found for event'
            given()
                .get(ftHelper.photoCoveragePath(savedEventId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(photoCoverageId1))
                .body('[0].photographer.id', notNullValue())
                .body('[0].photographer.nickname', equalTo(addCoverageForm1.nick))
                .body('[0].photographer.ratings', empty())
                .body('[0].description', equalTo(addCoverageForm1.description))
                .body('[0].link', equalTo(addCoverageForm1.link))
                .body('[0].best_photo', notNullValue())
                .body('[1].id', equalTo(photoCoverageId2))
                .body('[1].photographer.id', notNullValue())
                .body('[1].photographer.nickname', equalTo(addCoverageForm2.nick))
                .body('[1].photographer.ratings', empty())
                .body('[1].description', equalTo(addCoverageForm2.description))
                .body('[1].link', equalTo(addCoverageForm2.link))
                .body('[1].best_photo', notNullValue())
    }

    def 'GET /api/events/{event_id}/photo_coverage/{photo_coverage_id} should get photo coverages by given id, for event'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        expect: 'Photo coverage is found for given id'
            given()
                .get(ftHelper.photoCoveragePath(savedEventId, photoCoverageId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photoCoverageId))
                .body('photographer.id', notNullValue())
                .body('photographer.nickname', equalTo(addCoverageForm.nick))
                .body('photographer.ratings', empty())
                .body('description', equalTo(addCoverageForm.description))
                .body('link', equalTo(addCoverageForm.link))
                .body('best_photo', notNullValue())
    }

    def 'POST /api/events/{event_id}/photo_coverages should add new photo coverage to event'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        when: 'Create photo coverage for event'
            def addCoverageForm = randomAddCoverageForm()
            def response = given()
                .multiPart("coverage_data", addCoverageForm, 'application/json')
                .multiPart('best_photo', 'best_photo.jpeg', [] as byte[], 'image/jpeg')
                .when()
                .post(ftHelper.photoCoveragePath(savedEventId))
                .andReturn()
        then: 'Photo coverage is stored in database'
            response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body('id', notNullValue())
                .body('photographer.id', notNullValue())
                .body('photographer.nickname', equalTo(addCoverageForm.nick))
                .body('photographer.ratings', empty())
                .body('description', equalTo(addCoverageForm.description))
                .body('link', equalTo(addCoverageForm.link))
                .body('best_photo', notNullValue())
        and: 'Photo coverage id is returned in Location header'
            def photoCoverageIdFromLocationHeader = getIdFromLocationHeader(response)
            def photoCoverageIdFromResponseBody = getIdFromResponseBody(response)
            photoCoverageIdFromLocationHeader == photoCoverageIdFromResponseBody
    }

    def 'PATCH /api/events/{event_id}/photo_coverages/{photo_coverage_id} should update photo coverage'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        when: 'Update photo coverage'
            def updateCoverageForm = randomUpdatePhotoCoverageForm()
            given()
                .multiPart("coverage_data", updateCoverageForm, 'application/json')
                .multiPart('best_photo', 'best_photo.jpeg', [] as byte[], 'image/jpeg')
                .when()
                .patch(ftHelper.photoCoveragePath(savedEventId, photoCoverageId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photoCoverageId))
                .body('photographer.id', notNullValue())
                .body('photographer.nickname', equalTo(addCoverageForm.nick))
                .body('photographer.ratings', empty())
                .body('description', equalTo(updateCoverageForm.description))
                .body('link', equalTo(updateCoverageForm.link))
                .body('best_photo', notNullValue())
        then: 'Updated photo coverage is stored in database'
            given()
                .get(ftHelper.photoCoveragePath(savedEventId, photoCoverageId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photoCoverageId))
                .body('photographer.id', notNullValue())
                .body('photographer.nickname', equalTo(addCoverageForm.nick))
                .body('photographer.ratings', empty())
                .body('description', equalTo(updateCoverageForm.description))
                .body('link', equalTo(updateCoverageForm.link))
                .body('best_photo', notNullValue())
    }

    def 'DELETE /api/events/{event_id}/photo_coverages/{photo_coverage_id} should remove photo coverage from event'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        when: 'Remove photo coverage from database'
            given()
                .when()
                .delete(ftHelper.photoCoveragePath(savedEventId, photoCoverageId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        then: 'Photo coverage is no longer stored in database'
            given()
                .get(ftHelper.photoCoveragePath(savedEventId, photoCoverageId))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
    }
}
