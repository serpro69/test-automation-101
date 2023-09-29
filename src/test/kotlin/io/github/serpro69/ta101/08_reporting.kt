package io.github.serpro69.ta101

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

class Tests08 {

    @Test
    fun `failing test - finding pet by status`() {
        given()
            .baseUri("http://localhost:8080/api/v3")
            .accept(ContentType.JSON)
            .queryParam("status", "na")
        .`when`()
            .get("/pet/findByStatus/")
        .then()
            .statusCode(200)
    }

    @Test
    fun `failing test - finding pet by id`() {
        given()
            .baseUri("http://localhost:8080/api/v3")
            .accept(ContentType.JSON)
        .`when`()
            .get("/pet/10")
        .then()
            .body("id", equalTo("10"))
    }
}
