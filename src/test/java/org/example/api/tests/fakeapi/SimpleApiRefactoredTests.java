package org.example.api.tests.fakeapi;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.example.api.models.fakeapiuser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$x;
import static io.restassured.RestAssured.given;
import static org.example.api.assertions.Conditions.hasGoodZip;
import static org.example.api.assertions.Conditions.hasStatusCode;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(RestExtension.class)
public class SimpleApiRefactoredTests {

    private static UserService userService;

    @BeforeAll
    public static void initServices() {
        userService = new UserService();
    }

    @Test
    public void getSingleUserTest() {
        int userId = 5;
        userService.getUser(userId)
                .should(hasStatusCode(200))
                .should(hasGoodZip("address.zipcode"));
    }

    @Test
    public void getAllUsersTest() {
        given().get("/users")
                .then()
                .statusCode(200);
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    public void getAllUsersWithLimitTest(int limitSize) {
        List<UserRoot> users = given()
                .queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<UserRoot>>() {
                });

        Assertions.assertEquals(limitSize, users.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 40})
    public void getAllUsersWithLimitErrorParams(int limitSize) {
        List<UserRoot> users = given()
                .queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<UserRoot>>() {
                });
        Assertions.assertNotEquals(limitSize, users.size());
    }

    @Test
    public void getAllUsersSortByDescTest() {
        String sortType = "desc";
        List<UserRoot> usersSorted = given()
                .queryParam("sort", sortType)
                .get("/users")
                .then()
                .extract().as(new TypeRef<List<UserRoot>>() {
                });

        List<UserRoot> usersNotSorted = given()
                .get("/users")
                .then()
                .extract().as(new TypeRef<List<UserRoot>>() {
                });

        List<Integer> sortedResponseIds = usersSorted
                .stream()
                .map(UserRoot::getId).collect(Collectors.toList());

        List<Integer> sortedByCode = usersNotSorted.stream()
                .map(UserRoot::getId)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Assertions.assertNotEquals(usersSorted, usersNotSorted);
        Assertions.assertEquals(sortedResponseIds, sortedByCode);
    }

    @Test
    public void addNewUserTest() {
        UserRoot user = getTestUser();

        Integer userId = given().body(user)
                .post("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("id");

        Assertions.assertNotNull(userId);
    }

    @Test
    public void updateUserTest() {
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();

        Integer userId = given().body(user)
                .post("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("id");

        user.setPassword("newpass111");

        UserRoot updatedUser = given()
                .body(user)
                .pathParam("userId", userId)
                .put("/users/{userId}")
                .then().extract().as(UserRoot.class);

        Assertions.assertNotEquals(updatedUser.getPassword(), oldPassword);
    }

    private UserRoot getTestUser() {
        Random random = new Random();
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.3123", "81.1231");

        Address address = Address.builder()
                .city("Moscow")
                .number(random.nextInt(100))
                .zipcode("54231-4231")
                .street("Noviy Arbat 12")
                .geolocation(geolocation).build();

        return UserRoot.builder()
                .name(name)
                .phone("791237192")
                .email("fakemail@gmail.com")
                .username("thomasadmin")
                .password("mycoolpassword")
                .address(address).build();
    }

    @Test
    public void authUserTest() {
        AuthData authData = new AuthData("jimmie_k", "klein*#%*");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }
}
