package org.example.api.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.example.api.models.swager.Info;
import org.junit.jupiter.api.Assertions;
import org.example.api.assertions.Condition;

@RequiredArgsConstructor
public class MessageCondition implements Condition {
    private final String expectedMessage;
    @Override
    public void check(ValidatableResponse response) {
        Info info = response.extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(expectedMessage, info.getMessage());
    }
}
