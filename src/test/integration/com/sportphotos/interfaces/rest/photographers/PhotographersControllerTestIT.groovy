package com.sportphotos.interfaces.rest.photographers

import com.sportphotos.domain.photographers.PhotographersRepository
import com.sportphotos.domain.photographers.PhotographersService
import com.sportphotos.interfaces.rest.ControllerTest
import io.restassured.http.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.sportphotos.domain.model.AddRatingFormMock.randomAddRatingForm
import static com.sportphotos.domain.model.PhotographerMock.randomPhotographer
import static com.sportphotos.domain.model.RatingMock.randomRating
import static com.sportphotos.domain.model.UpdateRatingFormMock.randomUpdateRatingForm
import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.notNullValue

@ControllerTest
class PhotographersControllerTestIT extends Specification {

    @LocalServerPort
    int port

    @Autowired
    PhotographersService photographersServiceMock

    @Autowired
    PhotographersRepository photographersRepositoryMock

    def 'GET /api/photographers should get all photographers'() {
        given:
            def photographer1 = randomPhotographer()
            def photographer2 = randomPhotographer()

            photographersRepositoryMock.findAll() >> [photographer1, photographer2]
        expect:
            given()
                .get(photographersPath())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(photographer1.id))
                .body('[1].id', equalTo(photographer2.id))
    }

    def 'GET /api/photographers/{photographer_id} should get photographer with given id'() {
        given:
            def photographer = randomPhotographer()

            photographersServiceMock.findById(photographer.id) >> photographer
        expect:
            given()
                .get(photographersPath(photographer.id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photographer.id))
                .body('nickname', equalTo(photographer.nickname))
                .body('ratings', notNullValue())
    }

    def 'GET /api/photographers/{photographer_id}/ratings should get all ratings for photographer'() {
        given:
            def photographer = randomPhotographer()

            photographersServiceMock.getAllRatings(photographer.id) >> photographer.getRatings()
        expect:
            given()
                .get(ratingsPath(photographer.id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('[0].id', equalTo(photographer.ratings[0].id))
                .body('[1].id', equalTo(photographer.ratings[1].id))
    }

    def 'GET /api/photographers/{photographer_id}/ratings/{rating_id} should get rating by given id, for photographer'() {
        given:
            def photographer = randomPhotographer()

            photographersServiceMock.getRating(photographer.id, photographer.ratings[0].id) >> photographer.ratings[0]
        expect:
            given()
                .get(ratingsPath(photographer.id, photographer.ratings[0].id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(photographer.ratings[0].id))
                .body('rate', equalTo(photographer.ratings[0].rate))
                .body('comment', equalTo(photographer.ratings[0].comment))
    }

    def 'POST /api/photographers/{photographer_id}/ratings should add rating to photographer'() {
        given:
            def addRatingForm = randomAddRatingForm()
            def photographer = randomPhotographer()
            def rating = randomRating(rate: addRatingForm.rate, comment: addRatingForm.comment)
            photographersServiceMock.rate(photographer.id, addRatingForm) >> rating
            def request = """
                {
                    "rate": $addRatingForm.rate,
                    "comment": "$addRatingForm.comment"
                }
            """
        expect:
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post(ratingsPath(photographer.id))
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header('Location', "${ratingsPath(photographer.id, rating.id)}")
                .body('id', equalTo(rating.id))
                .body('rate', equalTo(rating.rate))
                .body('comment', equalTo(rating.comment))
    }

    def 'PATCH /api/photographers/{photographer_id}/ratings/{rating_id} should update rating of photographer'() {
        given:
            def updateRatingForm = randomUpdateRatingForm()
            def photographer = randomPhotographer()
            def rating = randomRating(rate: updateRatingForm.rate, comment: updateRatingForm.comment)
            photographersServiceMock.updateRate(photographer.id, rating.id, updateRatingForm) >> rating
            def request = """
                {
                    "rate": $updateRatingForm.rate,
                    "comment": "$updateRatingForm.comment"
                }
            """
        and:
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .patch(ratingsPath(photographer.id, rating.id))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body('id', equalTo(rating.id))
                .body('rate', equalTo(rating.rate))
                .body('comment', equalTo(rating.comment))
    }

    def photographersPath() {
        "http://localhost:$port/api/photographers"
    }

    def photographersPath(String photographerId) {
        "${photographersPath()}/$photographerId"
    }

    def ratingsPath(String photographerId) {
        "${photographersPath(photographerId)}/ratings"
    }

    def ratingsPath(String photographerId, String ratingId) {
        "${ratingsPath(photographerId)}/$ratingId"
    }
}
