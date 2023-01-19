package client;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pojo.User;

import static io.restassured.RestAssured.*;

public class UserClient {
    @Step("Создание user")
    public Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Config.URL)
                .body(user).post(Config.REGISTER);
    }

    @Step("Remove user")
    public void removeUser(String token) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(Config.URL).delete(Config.USER);
    }

    @Step("Login user")
    public Response loginUser(User user, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(Config.URL)
                .body(user).post(Config.LOGIN);
    }

    @Step("Login user")
    public Response loginUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Config.URL)
                .body(user).post(Config.LOGIN);
    }

    @Step("Update User")
    public Response updateUser(User newUser, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(Config.URL)
                .body(newUser).patch(Config.USER);
    }
}
