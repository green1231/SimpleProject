package org.example.api.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.example.api.assertions.Condition;


@RequiredArgsConstructor
public class ObjectCondition implements Condition {
    private final Object object;

    @Override
    public void check(ValidatableResponse response) {
        Object actualResult = response.extract().as(object.getClass());
        Assertions.assertEquals(object, actualResult);
    }
}
