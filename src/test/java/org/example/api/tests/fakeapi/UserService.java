package org.example.api.tests.fakeapi;


import org.example.api.assertions.AssertableResponse;

import static io.restassured.RestAssured.given;

public class UserService {

    public AssertableResponse getUser(int userId) {
        return new AssertableResponse(
                given()
                        .pathParam("userId", userId)
                        .get("/users/{userId}")
                        .then()
        );
    }
}
