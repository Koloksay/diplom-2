package client;
import data.ClientData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class RegisterClient extends RestClient {
    private static final String CLIENT_REGISTER_PATH = "/api/auth/register";


    @Step("Send POST request to /api/auth/register")
    public ValidatableResponse createNewClient(ClientData client) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(client)
                .when()
                .post(CLIENT_REGISTER_PATH).then();
    }



}