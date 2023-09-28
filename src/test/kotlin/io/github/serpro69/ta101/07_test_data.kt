package io.github.serpro69.ta101

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.github.serpro69.kfaker.faker
import io.restassured.RestAssured.given
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Tests07 {

    @ParameterizedTest
    @MethodSource("petSource")
    fun `test parameterized post`(pet: Pet) {
        val response = request()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(pet)
            .`when`()
            .post("/pet")
            .then()

        response.statusCode(200)
        response.body("id", equalTo(42))
    }

    private fun petSource(): Stream<Pet> {
        return Stream.of(
            Pet(
                42,
                "mango",
                Category(1, "Dogs"),
                emptyList(),
                emptyList(),
                Status.AVAILABLE
            ),
            Pet(
                73,
                "kiwi",
                Category(1, "Dogs"),
                emptyList(),
                emptyList(),
                Status.AVAILABLE
            )
        )
    }

    @ParameterizedTest
    @MethodSource("fakerPetSource")
    fun `test parameterized post with fake data generation`(pet: Pet) {
        val response = request()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(pet)
            .`when`()
            .post("/pet")
            .then()

        response.statusCode(200)
        response.body("id", equalTo(42))
    }

    private fun fakerPetSource(): Stream<Pet> {
        val faker = faker {  }
        val dogs = List(10) {
            faker.randomProvider.randomClassInstance<Pet> {
                namedParameterGenerator("id") { faker.random.nextInt(100, 1000) }
                namedParameterGenerator("name") { faker.dog.name() }
                typeGenerator<Category> {
                    Category(1, "Dogs")
                }
            }
        }
        return dogs.stream()
    }
}
