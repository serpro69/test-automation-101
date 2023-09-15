package io.github.serpro69.ta101

import io.restassured.RestAssured.get
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

class Tests01 {

    @Test
    fun `test get`() {
        get("http://localhost:8080/api/v3/pet/10")
            .then()
            .body("id", equalTo(10))
    }
}
