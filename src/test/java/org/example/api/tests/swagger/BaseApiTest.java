package org.example.api.tests.swagger;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.example.api.addons.AdminUserResolver;
import org.example.api.addons.CustomTpl;
import org.example.api.models.swager.FullUser;
import org.example.api.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.example.api.addons.RandomTestData.getRandomUser;

@ExtendWith(AdminUserResolver.class)
public class BaseApiTest {

    protected static UserService userService;
    protected FullUser randomUser;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        userService = new UserService();
    }
    @BeforeEach
    public void initTestUser() {
        randomUser = getRandomUser();
    }
}
