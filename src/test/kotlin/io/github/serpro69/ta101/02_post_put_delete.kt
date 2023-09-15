package io.github.serpro69.ta101

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

class Tests02 {

    @Test
    fun `test post`() {
        val response = given()
            .baseUri("http://localhost:8080/api/v3")
            .body(
                """
                {
                  "id": 42,
                  "name": "doggie",
                  "category": {
                    "id": 1,
                    "name": "Dogs"
                  },
                  "photoUrls": [
                    "string"
                  ],
                  "tags": [
                    {
                      "id": 0,
                      "name": "string"
                    }
                  ],
                  "status": "available"
                }
                """.trimIndent()
            )
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .post("/pet")
            .then()

            response.statusCode(200)
            response.body("id", equalTo(42))
    }

    @Test
    fun `test put`() {
        val response = given()
            .baseUri("http://localhost:8080/api/v3")
            .body(
                """
                {
                  "id": 42,
                  "name": "doggie",
                  "category": {
                    "id": 1,
                    "name": "Dogs"
                  },
                  "photoUrls": [
                    "string"
                  ],
                  "tags": [
                    {
                      "id": 0,
                      "name": "string"
                    }
                  ],
                  "status": "pending"
                }
                """.trimIndent()
            )
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .put("/pet")
            .then()

        response.statusCode(200)
        response.body("status", equalTo("pending"))
    }

    @Test
    fun `test delete`() {
        val response = given()
            .baseUri("http://localhost:8080/api/v3")
            .`when`()
            .delete("/pet/42")
            .then()

        response.statusCode(200)
        response.body(equalTo("Pet deleted"))
    }
}
