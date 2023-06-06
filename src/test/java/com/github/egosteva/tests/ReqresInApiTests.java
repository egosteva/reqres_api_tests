package com.github.egosteva.tests;

import com.github.egosteva.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.egosteva.specs.Specifications.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("API тесты для сайта Reqres.in")
public class ReqresInApiTests {

    @Test
    @DisplayName("Создание пользователя")
    void createUserTest() {
        String userName = "morpheus",
                userJob = "leader";

        CreateUserBodyModel createUserBody = new CreateUserBodyModel();
        createUserBody.setName(userName);
        createUserBody.setJob(userJob);
        CreateUserResponseModel createUserResponse = step("Отправить запрос на создание пользователя", () ->
                given(requestSpec)
                        .body(createUserBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class));

        step("Проверить имя пользователя в теле ответа", () ->
                assertThat(createUserResponse.getName()).isEqualTo(userName));
        step("Проверить работу пользователя в теле ответа", () ->
                assertThat(createUserResponse.getJob()).isEqualTo(userJob));
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void updateUserModelsTest() {
        String userJob = "zion resident";
        UpdateUserBodyModel updateUserBody = new UpdateUserBodyModel();
        updateUserBody.setJob(userJob);
        UpdateUserResponseModel updateUserResponse = step("Отправить запрос на обновление пользователя", () ->
                given(requestSpec)
                        .body(updateUserBody)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(updateUserResponseSpec)
                        .extract().as(UpdateUserResponseModel.class));

        step("Проверить работу пользователя в теле ответа", () ->
                assertThat(updateUserResponse.getJob()).isEqualTo(userJob));
    }

    @Test
    @DisplayName("Неуспешная регистрация")
    void unsuccessfulRegisterModelsTest() {
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

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserModelsTest() {
        step("Сделать запрос на удаление пользователя и проверить статус-код ответа", () ->
                given(requestSpec)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(deleteUserResponseSpec));
    }

    @Test
    @DisplayName("Получение списка пользователей")
    void checkUsersListModelsTest() {
        CheckUsersListResponseModel checkUsersListResponse = step("Сделать запрос на получение списка пользователей", () ->
                given(requestSpec)
                        .get("/users?page=2")
                        .then()
                        .spec(checkUsersListResponseSpec)
                        .extract().as(CheckUsersListResponseModel.class));
        List<DataResponseModel> dataListResponse = checkUsersListResponse.getData();
        SupportResponseModel supportResponse = checkUsersListResponse.getSupport();
        step("Проверить номер страницы в ответе", () ->
                assertThat(checkUsersListResponse.getPage()).isEqualTo(2));
        step("Проверить id первого пользователя в списке", () ->
                assertThat(dataListResponse.get(0).id).isEqualTo(7));
        step("Проверить email первого пользователя в списке", () ->
                assertThat(dataListResponse.get(0).email).isEqualTo("michael.lawson@reqres.in"));
        step("Проверить first_name первого пользователя в списке", () ->
                assertThat(dataListResponse.get(0).first_name).isEqualTo("Michael"));
        step("Проверить last_name первого пользователя в списке", () ->
                assertThat(dataListResponse.get(0).last_name).isEqualTo("Lawson"));
        step("Проверить аватар первого пользователя в списке", () ->
                assertThat(dataListResponse.get(0).avatar).isEqualTo("https://reqres.in/img/faces/7-image.jpg"));
        step("Проверить адрес support url", () ->
                assertThat(supportResponse.getUrl()).isEqualTo("https://reqres.in/#support-heading"));
    }
}