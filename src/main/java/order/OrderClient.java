package order;

import client.RestClient;
import com.google.gson.Gson;
import data.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_PATH = "/api/orders";

    @Step("Send POST request to /api/orders")
    public ValidatableResponse createOrder(String bearerToken, List<String> ingredients) {
        OrderRequest orderRequest = new OrderRequest(ingredients);
        return given()
                .spec(requestSpecification())
                .header("Authorization", bearerToken)
                .and()
                .body(new Gson().toJson(orderRequest))
                .when()
                .post(ORDER_PATH)
                .then();
    }
    @Step("Send GET request to /api/orders")
    public ValidatableResponse getClientsOrder(String bearerToken) {

        return given()
                .spec(requestSpecification())
                .header("Authorization", bearerToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

}