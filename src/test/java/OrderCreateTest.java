import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import client.UserClient;
import pojo.*;

import static org.hamcrest.Matchers.*;

public class OrderCreateTest {
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private Order order;
    private String token;
    private Response response;

    @Test
    @DisplayName("Creating an order without authorization")
    @Description("Создание заказа без авторизации")
    public void shouldCreateOrderWithoutAuth() {
        response = orderClient.createOrder(Order.getOrderCorrect(), "token");
        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order with authorization")
    @Description("Создание заказа с авторизацией")
    public void shouldCreateOrderWithAuth() {
        User user = User.createRandomUser();
        response = userClient.createUser(user);
        token = response.then().extract().body().path("accessToken");
        response = orderClient.createOrder(Order.getOrderCorrect(), token);
        userClient.removeUser(token);
        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutIngrShouldBeError() {
        response = orderClient.createOrder(Order.getOrderEmpty(), "token");
        response.then().assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Create order with ingredients")
    @Description("Создание заказа с ингредиентами")
    public void shouldCreateOrderWithIngr() {
        order = Order.getOrderCorrect();
        response = orderClient.createOrder(order, "token");
        response.then().assertThat().body("success", equalTo(true))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Create order with wrong hash ")
    @Description("Создание заказа с неверным хешем")
    public void createOrderWithWrongIngrHashShouldBeError() {
        order = Order.getOrderWrongHash();
        response = orderClient.createOrder(order, "token");
        response.then().assertThat().statusCode(500);
    }
}
