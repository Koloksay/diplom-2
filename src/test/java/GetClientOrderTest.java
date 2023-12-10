import client.ClientClient;
import client.RegisterClient;
import data.ClientData;
import data.ClientGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.IngredientsClient;
import order.OrderClient;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class GetClientOrderTest {

    RegisterClient registerClient;
    ClientClient clientClient;
    OrderClient orderClient;
    ClientData client;
    String bearerToken;
    List<String> ingredientIds;
    IngredientsClient ingredientsClient;

    @Before
    public void setup() {
        registerClient = new RegisterClient();
        clientClient = new ClientClient();
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();

        //создали нового клиента
        client = ClientGenerator.getRandomClient();
        ValidatableResponse response = registerClient.createNewClient(client);
        //получили токен для последующего удаления клиента
        bearerToken = response.extract().path("accessToken");
        //получили список всех существующий ингредиентов
        ingredientIds = ingredientsClient.getAllIngredientsIds();
        // Создаем два заказа
        for (int i = 0; i < 2; i++) {
            orderClient.createOrder(bearerToken, ingredientIds);
        }
    }

    @After
    //если token isNull не удаляем, если не Null - удаляем
    public void cleanUp() {
        if (bearerToken != null) {
            clientClient.deleteClient(bearerToken);
        }
    }
    @DisplayName("Авторизованному пользователю можно получить свои заказы")
    @Test
    public void authorizedUserCanGetOrders() {

        ValidatableResponse response = orderClient.getClientsOrder(bearerToken);

        // Проверяем, что статус ответа 200 ОК
        response.statusCode(HttpStatus.SC_OK);

    }
    @DisplayName("Авторизованному пользователю можно получить свои заказы")
    @Test
    public void authorizedUserCanGetOnlyLast50Orders() {
        // Создаем дополнительно 50 заказов
        for (int i = 0; i < 52; i++) {
            orderClient.createOrder(bearerToken, ingredientIds);
        }

        ValidatableResponse response = orderClient.getClientsOrder(bearerToken);

        // Проверяем, что количество заказов не превышает 50
        response.body("orders.size()", is(50));
    }
    @DisplayName("Авторизованному пользователю можно получить свои заказы")
    @Test
    public void NonAuthorizedUserCantGetOrders() {
        ValidatableResponse response = orderClient.getClientsOrder("");

        // Проверяем, что статус ответа 200 ОК
        response.statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}