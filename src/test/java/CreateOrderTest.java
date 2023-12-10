import client.ClientClient;
import client.RegisterClient;
import data.ClientData;
import data.ClientGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.IngredientsClient;
import order.OrderClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {

    RegisterClient registerClient;
    ClientClient clientClient;
    OrderClient orderClient;
    ClientData client;
    String bearerToken;
    List<String> ingredientIds;
    List<String> incorrectIngredientIds;
    IngredientsClient ingredientsClient;

    @Before
    public void setup() {
        registerClient = new RegisterClient();
        clientClient = new ClientClient();
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();

        client = ClientGenerator.getRandomClient();
        ValidatableResponse response = registerClient.createNewClient(client);
        bearerToken = response.extract().path("accessToken");
        ingredientIds = ingredientsClient.getAllIngredientsIds();
    }

    @After
    //если token isNull не удаляем, если не Null - удаляем
    public void cleanUp() {
        if (bearerToken != null) {
            clientClient.deleteClient(bearerToken);
        }
    }
    @DisplayName("Авторизованному пользователю можно создать заказ. Код ответа сервера 200 ОК")
    @Test
    public void OrderCanBeCreated() {

        ValidatableResponse response = orderClient.createOrder(bearerToken, ingredientIds);

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_OK);
        // Проверка тела ответа
        response.assertThat()
                .body("success", is(true));
    }
    @DisplayName("Неавторизованному пользователю нельзя создать заказ. Код ответа сервера 401")
    @Test
    public void NonAuthClientCantCreateOrder() {

        ValidatableResponse response = orderClient.createOrder("", ingredientIds);

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
        // Проверка тела ответа
        response.assertThat()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
    @DisplayName("Авторизованному пользователю нельзя создать заказ без ингредиентов")
    @Test
    public void OrderCantBeCreatedWithoutIngredients() {

        ValidatableResponse response = orderClient.createOrder(bearerToken, Collections.emptyList());

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        // Проверка тела ответа
        response.assertThat()
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }
    @DisplayName("Неавторизованному пользователю нельзя создать заказ без ингредиентов")
    @Test
    public void NonAuthClientCantCreatedOrderWithoutIngredients() {

        ValidatableResponse response = orderClient.createOrder("", Collections.emptyList());

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        // Проверка тела ответа
        response.assertThat()
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @DisplayName("Авторизованному пользователю нельзя создать заказ с некорректными ингредиентами")
    @Test
    public void OrderCantBeCreatedWithIncorrectIngredients() {
        incorrectIngredientIds = new ArrayList<>();
        incorrectIngredientIds.add(RandomStringUtils.randomAlphabetic(24));
        ValidatableResponse response = orderClient.createOrder(bearerToken, incorrectIngredientIds);

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @DisplayName("Неавторизованному пользователю нельзя создать заказ с некорректными ингредиентами")
    @Test
    public void NonAuthClientCantCreateOrderWithIncorrectIngredients() {
        incorrectIngredientIds = new ArrayList<>();
        incorrectIngredientIds.add(RandomStringUtils.randomAlphabetic(24));
        ValidatableResponse response = orderClient.createOrder("", incorrectIngredientIds);

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}