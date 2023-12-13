package client;

import data.ClientData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ClientClient extends RestClient{
    private static final String CLIENT_PATH = "/api/auth/user";



    @Step("Send GET request to /api/auth/user")
    public ValidatableResponse getInfoAboutClient(String bearerToken) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", bearerToken)
                .when()
                .get(CLIENT_PATH)
                .then();
    }

    @Step("Send PATCH request to /api/auth/user")
    public ValidatableResponse editInfoAboutClient(String bearerToken, ClientData client) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", bearerToken)
                .body(client)
                .when()
                .patch(CLIENT_PATH)
                .then();
    }

    @Step("Send DELETE request to /api/auth/user")
    public ValidatableResponse deleteClient(String bearerToken) {
        return given()
                .spec(requestSpecification())
                .auth().oauth2(bearerToken)
                .when()
                .delete(CLIENT_PATH)
                .then();
    }
}