package org.example.api.assertions;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GenericAssertableResponse<T> {

    private final ValidatableResponse response;
    private final Class<T> clazz;

    public GenericAssertableResponse<T> should(Condition condition) {
        condition.check(response);
        return this;
    }

    public T as() {
        return response.extract().as(clazz);
    }

    public T asObject(String jsonPath) {
        return response.extract().jsonPath().getObject(jsonPath, clazz);
    }

    public <E> E as(Class<E> tClass) {
        return response.extract().as(tClass);
    }

    public <E> List<E> asList(Class<E> out) {
        return response.extract().jsonPath().getList("", out);
    }


    public List<T> asListObject() {
        return response.extract().jsonPath().getList("", clazz);
    }

    public List<T> asListObject(String jsonPath) {
        return response.extract().jsonPath().getList(jsonPath, clazz);
    }

    public Response asResponse() {
        return response.extract().response();
    }
}
