package org.tigz.nostrelay;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testVersionEndpoint() {
        String server = given()
                .when().get("/version")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("server");

        assertThat(server).isEqualTo("arango");
    }

}