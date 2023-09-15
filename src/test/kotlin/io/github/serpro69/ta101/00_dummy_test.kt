package io.github.serpro69.ta101

import io.restassured.RestAssured.get
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

class Tests00 {

    @Test
    fun `first api test`() {
        get("/lotto") // Send a GET request to /lotto
            .then() // proceed to validating response
            .body("lotto.lottoId", equalTo(5)) // assert on the field of response body
    }
}
