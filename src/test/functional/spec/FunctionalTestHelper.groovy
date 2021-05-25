package spec

import com.sportphotos.domain.events.AddCoverageForm
import com.sportphotos.domain.events.AddEventForm
import com.sportphotos.domain.photographers.AddRatingForm
import io.restassured.http.ContentType
import org.springframework.boot.test.context.TestComponent
import org.springframework.http.HttpStatus

import static io.restassured.RestAssured.given

@TestComponent
class FunctionalTestHelper {

    int port

    FunctionalTestHelper(int port) {
        this.port = port
    }

    def createEvent(AddEventForm addEventForm, byte[] avatar) {
        def response = given()
            .multiPart("event_data", addEventForm, 'application/json')
            .multiPart('avatar', 'avatar.jpeg', avatar, 'image/jpeg')
            .when()
            .post(path())
            .andReturn()

        response.then().statusCode(HttpStatus.CREATED.value())

        response.getHeader('Location').split('/').last()
    }

    def createPhotoCoverageForEvent(String eventId, AddCoverageForm addCoverageForm, byte[] bestPhoto) {
        def response = given()
            .multiPart("coverage_data", addCoverageForm, 'application/json')
            .multiPart('best_photo', 'best_photo.jpeg', bestPhoto, 'image/jpeg')
            .when()
            .post(photoCoveragePath(eventId))
            .andReturn()

        response.then().statusCode(HttpStatus.CREATED.value())

        response.getHeader('Location').split('/').last()
    }

    def getPhotographerId(String eventId, String photoCoverageId) {
        def response = given()
            .get(photoCoveragePath(eventId, photoCoverageId))
            .andReturn()

        response.then().statusCode(HttpStatus.OK.value())

        response.body().jsonPath().getString('photographer.id')
    }

    def createRating(String photographerId, AddRatingForm addRatingForm) {
        def response = given().contentType(ContentType.JSON)
            .body(addRatingForm)
            .post(ratingsPath(photographerId))
            .andReturn()

        response.then().statusCode(HttpStatus.CREATED.value())

        response.getHeader('Location').split('/').last()
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

    static def getIdFromLocationHeader(def response) {
        response.getHeader('Location').split('/').last()
    }

    static def getIdFromResponseBody(def response) {
        response.body().jsonPath().getString('id')
    }
}
