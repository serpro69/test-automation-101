package io.github.serpro69.ta101

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.restassured.RestAssured.given
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val request: () -> RequestSpecification = {
    val cfg = RestAssuredConfig.config()
        .objectMapperConfig(ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory { _, _ ->
            jacksonMapperBuilder()
                .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .build()
        })
    given()
        .config(cfg)
        .baseUri("http://localhost:8080/api/v3")
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

class Tests06 {

    @Test
    fun `test with serialization of response body`() {
        val pet = request()
            .accept(ContentType.JSON)
            .`when`()
            .get("/pet/10")
            .then()
            .extract().body()
            .`as`(Pet::class.java)

        assertEquals(pet.id, 10)
    }
}

data class Pet(
    val id: Int,
    val name: String,
    val category: Category,
    val photoUrls: List<String>,
    val tags: List<Tag>,
    val status: Status,
)

data class Category(
    val id: Int,
    val name: String,
)

data class Tag(
    val id: Int,
    val name: String,
)

enum class Status {
    AVAILABLE,
    PENDING,
    SOLD,
    ;

    @JsonCreator
    override fun toString(): String = name.lowercase()
}
