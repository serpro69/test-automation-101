package io.github.serpro69.ta101

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

private val request: () -> RequestSpecification = {
    given().baseUri("http://localhost:8080/api/v3")
}

private val jsonBodyString: (id: Int, status: String) -> String = { id, status ->
    """
    {
      "id": $id,
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
      "status": "$status"
    }
    """.trimIndent()
}

private val RequestSpecification.jsonBody: (id: Int, status: String) -> RequestSpecification
    get() = { id, status ->
        body(jsonBodyString(id, status))
            .contentType(ContentType.JSON)
    }

class Tests03 {

    @Test
    fun `test post`() {
        val response = request()
            .body(jsonBodyString(42, "available"))
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
        val response = request()
            .jsonBody(42, "pending")
            .accept(ContentType.JSON)
            .`when`()
            .put("/pet")
            .then()

        response.statusCode(200)
        response.body("status", equalTo("pending"))
    }

    @Test
    fun `test delete`() {
        val response = request()
            .`when`()
            .delete("/pet/42")
            .then()

        response.statusCode(200)
        response.body(equalTo("Pet deleted"))
    }
}
