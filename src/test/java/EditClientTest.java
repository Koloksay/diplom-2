import client.ClientClient;
import client.RegisterClient;
import data.ClientData;
import data.ClientGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;


public class EditClientTest {

    ClientClient clientClient;
    RegisterClient registerClient;
    ClientData client;
    String bearerToken;
    String refreshToken;
    String email;
    String newEmail;
    String newName;
    String name;

    @Before
    public void setup() {
        registerClient = new RegisterClient();
        clientClient = new ClientClient();
        client = ClientGenerator.getRandomClient();
        ValidatableResponse response = registerClient.createNewClient(client);

        bearerToken = response.extract().path("accessToken");
        refreshToken = response.extract().path("refreshToken");
        email = client.getEmail().toLowerCase();
        name = client.getName();
    }
    @DisplayName("Авторизованный пользователь может изменить поле Email")
    @Test
    public void AuthClientCanEditEmail() {
        newEmail = ClientGenerator.generateRandomEmail();
        client.setEmail(newEmail);
        ValidatableResponse response = clientClient.editInfoAboutClient(bearerToken,client);
        //Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(newEmail.toLowerCase()))
                .body("user.name", is(name));
    }

    @DisplayName("Авторизованный пользователь может изменить поле Name")
    @Test
    public void AuthClientCanEditName() {
        newName = RandomStringUtils.randomAlphabetic(10);;
        client.setName(newName);
        ValidatableResponse response = clientClient.editInfoAboutClient(bearerToken,client);
        //Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(newName));
    }

    @DisplayName("Ошибка 401, если неавторизованный пользователь пытается изменить поле Name")
    @Test
    public void NonAuthClientCantEditName() {
        newName = RandomStringUtils.randomAlphabetic(10);;
        client.setName(newName);
        ValidatableResponse response = clientClient.editInfoAboutClient("",client);
        //Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @DisplayName("Ошибка 401, если неавторизованный пользователь пытается изменить поле Email")
    @Test
    public void NonAuthClientCantEditEmail() {
        newEmail = ClientGenerator.generateRandomEmail();
        client.setEmail(newEmail);
        ValidatableResponse response = clientClient.editInfoAboutClient("",client);
        //Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @After
    //если token isNull не удаляем, если не Null - удаляем
    public void cleanUp() {
        if (bearerToken != null) {
            clientClient.deleteClient(bearerToken);
        }
    }
}