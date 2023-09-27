package io.github.serpro69.ta101

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

enum class Auth {
    BASIC,
    API_KEY,
    NONE,
    ;
}

@Suppress("UastIncorrectHttpHeaderInspection")
private val request: (auth: Auth) -> RequestSpecification = {
    val r = given()
        .baseUri("http://localhost:8080/api/v3")
        .auth()

        when (it) {
            Auth.BASIC -> r.preemptive().basic("test", "abc123") // use preemptive to force sending auth token
            Auth.API_KEY -> r.none().header("api_key", "special-key")
            Auth.NONE -> r.none()
        }
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

class Tests05 {

    @Test
    fun `test basic auth`() {
        val response = request(Auth.BASIC)
            .jsonBody(42, "available")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .post("/pet")
            .then()

        response.statusCode(200)
        response.body("id", equalTo(42))
    }

    @Test
    fun `test token-based auth`() {
        val response = request(Auth.API_KEY)
            .jsonBody(42, "available")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .post("/pet")
            .then()

        response.statusCode(200)
        response.body("id", equalTo(42))
    }

    @Test
    fun `test no-auth`() {
        val response = request(Auth.NONE)
            .jsonBody(42, "available")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .post("/pet")
            .then()

        response.statusCode(200)
        response.body("id", equalTo(42))
    }
}
