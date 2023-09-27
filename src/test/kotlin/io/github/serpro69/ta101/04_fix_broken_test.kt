package io.github.serpro69.ta101

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

class Tests04 {

    @Test
    fun `test finding pet by status`() {
        given()
            .baseUri("http://localhost:8080/api/v3")
            .accept(ContentType.JSON)
            .queryParam("status", "na")
        .`when`()
            .get("/pet/findByStatus/")
        .then()
            .statusCode(200)
    }
}
