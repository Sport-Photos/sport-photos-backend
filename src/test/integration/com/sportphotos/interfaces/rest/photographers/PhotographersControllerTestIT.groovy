package com.sportphotos.interfaces.rest.photographers


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

@ControllerTest
class PhotographersControllerTestIT extends Specification {

    @LocalServerPort
    int port

    @Autowired
    PhotographersService photographersServiceMock

    def 'POST /api/photographers/{photographer_id}/ratings should add rating to photographer'() {
        given: 'Two events are stored in database'
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
        expect: 'Both events are returned'
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
        given: 'Two events are stored in database'
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
        and: 'rating is updated'
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

    def ratingsPath(String photographerId) {
        "http://localhost:$port/api/photographers/$photographerId/ratings"
    }

    def ratingsPath(String photographerId, String ratingId) {
        "http://localhost:$port/api/photographers/$photographerId/ratings/$ratingId"
    }
}
