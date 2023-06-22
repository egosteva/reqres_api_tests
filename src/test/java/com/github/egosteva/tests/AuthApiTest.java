package com.github.egosteva.tests;

import com.github.egosteva.models.UnsuccessfulRegisterBodyModel;
import com.github.egosteva.models.UnsuccessfulRegisterResponseModel;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.egosteva.specs.Specifications.requestSpec;
import static com.github.egosteva.specs.Specifications.unsuccessfulRegisterResponseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Feature("API тесты для сайта Reqres.in")
@Owner("egosteva")
@DisplayName("Проверка авторизации")
public class AuthApiTest {

    @Test
    @DisplayName("Неуспешная регистрация")
    void unsuccessfulRegisterTest() {
        String userEmail = "sydney@fife";

        UnsuccessfulRegisterBodyModel unsuccessfulRegisterBody = new UnsuccessfulRegisterBodyModel();
        unsuccessfulRegisterBody.setEmail(userEmail);
        UnsuccessfulRegisterResponseModel unsuccessfulRegisterResponse = step("Сделать запрос на регистрацию без указания пароля", () ->
                given(requestSpec)
                        .body(unsuccessfulRegisterBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(unsuccessfulRegisterResponseSpec)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));

        step("Проверить, что в ответе текст ошибки 'Missing password'", () ->
                assertThat(unsuccessfulRegisterResponse.getError()).isEqualTo("Missing password"));
    }
}