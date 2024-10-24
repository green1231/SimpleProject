package org.example.api.tests.fakeapi;

import org.example.api.assertions.Conditions;
import org.example.api.models.fakeapiuser.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@ExtendWith(RestExtension.class)
public class MyTests {

    private final UserService userService = new UserService();

    @Test
    public void getSingleUserTest2() {
        int userId = 5;

        userService.getUser(userId)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasGoodZip("address.zipcode"));
    }

    @Test
    public void getSingleUserTest() {
        int userId = 5;
        UserRoot response = given()
                .pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(UserRoot.class);

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }
}
