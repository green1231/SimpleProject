package org.example.api.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.example.api.assertions.Condition;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class JsonObjectCondition implements Condition {

    private final String jsonPath;
    private final Object expectedObject;

    @Override
    public void check(ValidatableResponse response) {
        Object actualObject = response.extract()
                .jsonPath()
                .getObject(jsonPath, expectedObject.getClass());
        Assertions.assertEquals(expectedObject, actualObject);
    }
}
