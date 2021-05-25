package spec

import com.sportphotos.integration.MongoTestHelper
import io.restassured.http.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.sportphotos.domain.model.AddCoverageFormMock.randomAddCoverageForm
import static com.sportphotos.domain.model.AddEventFormMock.randomAddEventForm
import static com.sportphotos.domain.model.AddRatingFormMock.randomAddRatingForm
import static com.sportphotos.domain.model.UpdateRatingFormMock.randomUpdateRatingForm
import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.notNullValue
import static org.hamcrest.Matchers.empty
import static spec.FunctionalTestHelper.getIdFromLocationHeader
import static spec.FunctionalTestHelper.getIdFromResponseBody

@FunctionalTest
class PhotographersFT extends Specification {

    @Autowired
    FunctionalTestHelper ftHelper

    @Autowired
    MongoTestHelper mongoHelper

    def setup() {
        mongoHelper.cleanDatabase()
    }

    def 'GET /api/photographers should get all photographers'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create two photo coverages for event, which also creates photographers'
            def addCoverageForm1 = randomAddCoverageForm()
            ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm1, [] as byte[])
            def addCoverageForm2 = randomAddCoverageForm()
            ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm2, [] as byte[])
        expect: 'Both photographers are found in database'
            given()
                .get(ftHelper.photographersPath())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', notNullValue())
                .body('[0].nickname', equalTo(addCoverageForm1.nick))
                .body('[0].ratings', empty())
                .body('[1].id', notNullValue())
                .body('[1].nickname', equalTo(addCoverageForm2.nick))
                .body('[1].ratings', empty())
    }

    def 'GET /api/photographers/{photographer_id} should get photographer with given id'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event, which also creates photographer'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        expect: 'Photographer is found in database'
            def photographerId = ftHelper.getPhotographerId(savedEventId, photoCoverageId)
            given()
                .get(ftHelper.photographersPath(photographerId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photographerId))
                .body('nickname', equalTo(addCoverageForm.nick))
                .body('ratings', empty())
    }

    def 'GET /api/photographers/{photographer_id}/ratings should get all ratings for photographer'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event, which also creates photographer'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        and: 'Crate two ratings for photographer'
            def photographerId = ftHelper.getPhotographerId(savedEventId, photoCoverageId)
            def addRatingForm1 = randomAddRatingForm()
            def ratingId1 = ftHelper.createRating(photographerId, addRatingForm1)
            def addRatingForm2 = randomAddRatingForm()
            def ratingId2 = ftHelper.createRating(photographerId, addRatingForm2)
        expect: 'Both ratings are found in database'
            given()
                .get(ftHelper.ratingsPath(photographerId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(ratingId1))
                .body('[0].rate', equalTo(addRatingForm1.rate))
                .body('[0].comment', equalTo(addRatingForm1.comment))
                .body('[1].id', equalTo(ratingId2))
                .body('[1].rate', equalTo(addRatingForm2.rate))
                .body('[1].comment', equalTo(addRatingForm2.comment))
    }

    def 'GET /api/photographers/{photographer_id}/ratings/{rating_id} should get rating by given id, for photographer'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event, which also creates photographer'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        and: 'Crate rating for photographer'
            def photographerId = ftHelper.getPhotographerId(savedEventId, photoCoverageId)
            def addRatingForm = randomAddRatingForm()
            def ratingId = ftHelper.createRating(photographerId, addRatingForm)
        expect: 'Rating is found in database'
            given()
                .get(ftHelper.ratingsPath(photographerId, ratingId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(ratingId))
                .body('rate', equalTo(addRatingForm.rate))
                .body('comment', equalTo(addRatingForm.comment))
    }

    def 'POST /api/photographers/{photographer_id}/ratings should add rating to photographer'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event, which also creates photographer'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        when: 'Create rating for photographer'
            def photographerId = ftHelper.getPhotographerId(savedEventId, photoCoverageId)
            def addRatingForm = randomAddRatingForm()

            def response = given().contentType(ContentType.JSON)
                .body(addRatingForm)
                .post(ftHelper.ratingsPath(photographerId))
                .andReturn()
        then: 'Rating is stored in database'
            response.then().statusCode(HttpStatus.CREATED.value())
                .body('id', notNullValue())
                .body('rate', equalTo(addRatingForm.rate))
                .body('comment', equalTo(addRatingForm.comment))
        and: 'Rating id is returned in Location header'
            def ratingIdFromLocationHeader = getIdFromLocationHeader(response)
            def ratingIdFromResponseBody = getIdFromResponseBody(response)
            ratingIdFromLocationHeader == ratingIdFromResponseBody
    }

    def 'PATCH /api/photographers/{photographer_id}/ratings/{rating_id} should update rating of photographer'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event, which also creates photographer'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        and: 'Create rating for photographer'
            def photographerId = ftHelper.getPhotographerId(savedEventId, photoCoverageId)
            def addRatingForm = randomAddRatingForm()
            def ratingId = ftHelper.createRating(photographerId, addRatingForm)
        when: 'Update rating for photographer'
            def updateRatingForm = randomUpdateRatingForm()

            given().contentType(ContentType.JSON)
                .body(updateRatingForm)
                .patch(ftHelper.ratingsPath(photographerId, ratingId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', notNullValue())
                .body('rate', equalTo(updateRatingForm.rate))
                .body('comment', equalTo(updateRatingForm.comment))

        then: 'Updated rating is stored in database'
            given()
                .get(ftHelper.ratingsPath(photographerId, ratingId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(ratingId))
                .body('rate', equalTo(updateRatingForm.rate))
                .body('comment', equalTo(updateRatingForm.comment))
    }

    def 'DELETE /api/photographers/{photographer_id}/ratings/{rating_id} should remove rating from photographer'() {
        given: 'Create event in database'
            def addEventForm = randomAddEventForm()
            def savedEventId = ftHelper.createEvent(addEventForm, [] as byte[])
        and: 'Create photo coverage for event, which also creates photographer'
            def addCoverageForm = randomAddCoverageForm()
            def photoCoverageId = ftHelper.createPhotoCoverageForEvent(savedEventId, addCoverageForm, [] as byte[])
        and: 'Create rating for photographer'
            def photographerId = ftHelper.getPhotographerId(savedEventId, photoCoverageId)
            def addRatingForm = randomAddRatingForm()
            def ratingId = ftHelper.createRating(photographerId, addRatingForm)
        when: 'Remove rating from database'
            given()
                .when()
                .delete(ftHelper.ratingsPath(photographerId, ratingId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        then: 'Rating is no longer stored in database'
            given()
                .get(ftHelper.ratingsPath(photographerId, ratingId))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
    }
}
