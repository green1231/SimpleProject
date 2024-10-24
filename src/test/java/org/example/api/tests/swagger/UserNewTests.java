package org.example.api.tests.swagger;


import io.restassured.response.Response;
import org.example.api.addons.AdminUser;
import org.example.api.models.swager.FullUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.example.api.addons.RandomTestData.getAdminUser;
import static org.example.api.assertions.Conditions.hasMessage;
import static org.example.api.assertions.Conditions.hasStatusCode;


public class UserNewTests extends BaseApiTest {

    @Test
    public void positiveRegisterTest() {
        userService.register(randomUser)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"));
    }

    @Test
    public void signUpWithSameData() {
        userService.register(randomUser)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"));

        userService.register(randomUser)
                .should(hasStatusCode(400))
                .should(hasMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        randomUser.setPass(null);

        userService.register(randomUser)
                .should(hasStatusCode(400))
                .should(hasMessage("Missing login or password"));
    }

    @Test
    public void positiveAdminAuthTest(@AdminUser FullUser admin) {
        String token = userService.auth(admin)
                .should(hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest() {
        userService.register(randomUser);

        String token = userService.auth(randomUser)
                .should(hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuthTest() {
        userService.auth(randomUser)
                .should(hasStatusCode(401));
    }

    @Test
    public void positiveGetUserInfoTest() {
        FullUser user = getAdminUser();
        String token = userService.auth(user).asJwt();

        userService.getUserInfo(token)
                .should(hasStatusCode(200));
    }

    @Test
    public void negativeGetUserInfoInvalidJwtTest() {
        userService.getUserInfo("fake jwt")
                .should(hasStatusCode(401));
    }

    @Test
    public void negativeGetUserInfoWithoutJwtTest() {
        userService.getUserInfo()
                .should(hasStatusCode(401));
    }

    @Test
    public void positiveChangeUserPassTest() {
        String oldPassword = randomUser.getPass();
        userService.register(randomUser);

        String token = userService.auth(randomUser).asJwt();

        String updatedPassValue = "newpassUpdated";

        userService.updatePass(updatedPassValue, token)
                .should(hasStatusCode(200))
                .should(hasMessage("User password successfully changed"));

        randomUser.setPass(updatedPassValue);

        token = userService.auth(randomUser).should(hasStatusCode(200)).asJwt();

        FullUser updatedUser = userService.getUserInfo(token).as(FullUser.class);

        Assertions.assertNotEquals(oldPassword, updatedUser.getPass());
    }

    @Test
    public void negativeChangeAdminPasswordTest() {
        FullUser user = getAdminUser();

        String token = userService.auth(user).asJwt();

        String updatedPassValue = "newpassUpdated";
        userService.updatePass(updatedPassValue, token)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant update base users"));
    }

    @Test
    public void negativeDeleteAdminTest() {
        var user = getAdminUser();

        String token = userService.auth(user).asJwt();

        userService.deleteUser(token)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant delete base users"));
    }

    @Test
    public void positiveDeleteNewUserTest() {
        userService.register(randomUser);

        String token = userService
                .auth(randomUser)
                .asJwt();

        userService.deleteUser(token)
                .should(hasStatusCode(200))
                .should(hasMessage("User successfully deleted"));
    }

    @Test
    public void positiveGetAllUsersTest() {
        List<String> users = userService.getAllUsers().asList(String.class);
        Assertions.assertTrue(users.size() >= 3);
    }
}
