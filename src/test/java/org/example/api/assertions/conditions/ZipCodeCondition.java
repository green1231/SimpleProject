package org.example.api.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.example.api.assertions.Condition;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class ZipCodeCondition implements Condition {
    private final String path;

    @Override
    public void check(ValidatableResponse response) {
        String zipCode = response.extract().jsonPath().getString(path);
        Assertions.assertTrue(zipCode.matches("\\d{5}-\\d{4}"));
    }
}
