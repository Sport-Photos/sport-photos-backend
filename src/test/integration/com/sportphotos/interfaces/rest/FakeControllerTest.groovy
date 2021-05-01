package com.sportphotos.interfaces.rest

import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static io.restassured.RestAssured.given

@ControllerTest
class FakeControllerTest extends Specification {

    @LocalServerPort
    int port

    def 'GET /fake should return fake object'() {
        expect:
            given()
                .when()
                .get("http://localhost:$port/fake")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
    }
}
