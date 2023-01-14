import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import client.UserClient;
import pojo.User;
import static org.hamcrest.Matchers.*;

public class RegistrationTest {
    private User user;
    private final UserClient userClient = new UserClient();
    private Response response;


    @Test
    @DisplayName("Create unique user")
    @Description("Создание уникального пользака")
    public void successRegTest() {
        user = User.createRandomUser();
        response = userClient.createUser(user);
        String token = response.then().extract().body().path("accessToken");
        userClient.removeUser(token);
        response.then().assertThat().body("accessToken", notNullValue())
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Create user, who was created")
    @Description("Создание не уникального пользака, который уже был")
    public void existRegTest() {
        user = User.getExistUser();
        response = userClient.createUser(user);
        response.then().assertThat().body("message", equalTo("User already exists"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Create user without name field")
    @Description("Создание пользака без заполенения поля имени")
    public void regWithoutNameTest() {
        user = User.createUserWithoutName();
        response = userClient.createUser(user);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Create user without filling field email")
    @Description("Создание пользака без заполнения поля почты")
    public void regWithoutEmailTest() {
        user = User.createUserWithoutEmail();
        response = userClient.createUser(user);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Create user without filling password field")
    @Description("Создание пользака без заполнения поля пароля")
    public void regWithoutPassword() {
        user = User.createUserWithoutPassword();
        response = userClient.createUser(user);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }
}
