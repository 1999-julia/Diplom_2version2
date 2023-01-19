import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.UserClient;
import pojo.User;

import static org.hamcrest.Matchers.*;

public class UpdateTest {
    private User user;
    private Response response;
    private final UserClient userClient = new UserClient();
    private String token;
    private String email;
    private String password;
    private String name;

    @Before
    public void setup() {
        user = User.createRandomUser();
        response = userClient.createUser(user);
        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Change email and name with authorization")
    @Description("Меняем email и name аккаунта с авторизацией")
    public void shouldUpdateEmailAndName() {
        email = user.getEmail();
        name = user.getName();
        user.setEmail(email + "email");
        user.setName(name + "name");
        response = userClient.updateUser(user, token);
        user.setEmail(email);
        user.setName(name);
        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Change account password with authorization")
    @Description("Меняем пароль аккаунта с авторизацией")
    public void shouldUpdatePassword() {
        password = user.getPassword();
        user.setPassword(password + "password");
        response = userClient.updateUser(user, token);
        user.setPassword(password);
        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Change email and account name without authorization")
    @Description("Меняем почту и имя аккаунта без авторизации")
    public void updateEmailAndNameShouldBeError() {
        email = user.getEmail();
        name = user.getName();
        user.setEmail(email + "email");
        user.setName(name + "name");
        response = userClient.updateUser(user, "null");
        user.setEmail(email);
        user.setName(name);
        response.then().assertThat().body("success", equalTo(false))
                .and().statusCode(401);
    }

    @Test
    @DisplayName("Change the account password without authorization")
    @Description("Меняем пароль аккаунта без авторизации")
    public void updatePasswordShouldBeError() {
        password = user.getPassword();
        user.setPassword(password + "password");
        response = userClient.updateUser(user, "null");
        user.setPassword(password);
        response.then().assertThat().body("success", equalTo(false))
                .and().statusCode(401);
    }

    @After
    public void teardown() {
        userClient.removeUser(token);
    }
}
