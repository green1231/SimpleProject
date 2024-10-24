package org.example.api.assertions;


import org.example.api.assertions.conditions.*;

public class Conditions {

    public static MessageCondition hasMessage(String expectedMessage) {
        return new MessageCondition(expectedMessage);
    }

    public static StatusCodeCondition hasStatusCode(Integer expectedStatus) {
        return new StatusCodeCondition(expectedStatus);
    }

    public static ZipCodeCondition hasGoodZip(String path) {
        return new ZipCodeCondition(path);
    }

    public static ObjectCondition equalToObject(Object object) {
        return new ObjectCondition(object);
    }

    public static JsonObjectCondition equalToObject(String jsonPath, Object object) {
        return new JsonObjectCondition(jsonPath, object);
    }
}
