import client.ClientClient;
import client.RegisterClient;
import client.LoginClient;
import data.ClientData;
import data.ClientGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.Is.isA;

public class LoginTest {

    LoginClient loginClient;
    ClientClient clientClient;
    RegisterClient registerClient;
    ClientData client;
    String bearerToken;
    String email;
    String name;

    @Before
    public void setup() {
        registerClient = new RegisterClient();
        clientClient = new ClientClient();
        client = ClientGenerator.getRandomClient();
        ValidatableResponse response = registerClient.createNewClient(client);

        // записываем данные для дальнейшего использования
        bearerToken = response.extract().path("accessToken");
        email = client.getEmail().toLowerCase();
        name = client.getName();
    }

    @After
    //если token isNull не удаляем, если не Null - удаляем
    public void cleanUp() {
        if (bearerToken != null) {
            clientClient.deleteClient(bearerToken);
        }
    }
    @DisplayName("Успешная авторизация при корректных полях")
    @Test
    public void ClientCanAuthorization() {
        // Авторизация
        loginClient = new LoginClient();
        ValidatableResponse response = loginClient.loginClient(client);

        // Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("accessToken", isA(String.class))
                .body("refreshToken", isA(String.class))
                .body("user.email", is(email))
                .body("user.name", is(name));
    }

    @DisplayName("Ошибка 401 при попытке авторизации при неверном значении в поле 'email' ")
    @Test
    public void ClientCantAuthorizationWithInvalidEmail() {
        // Авторизация с неверным email
        loginClient = new LoginClient();
        client.setEmail("invalidName@invalid.com");
        ValidatableResponse response = loginClient.loginClient(client);

        // Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));

    }

    @DisplayName("Ошибка 401 при попытке авторизации при неверном значении в поле 'password' ")
    @Test
    public void ClientCantAuthorizationWithInvalidPasswordl() {
        // Авторизация c неверным паролем
        loginClient = new LoginClient();
        client.setEmail("invalidPassword");
        ValidatableResponse response = loginClient.loginClient(client);

        // Проверка статуса ответа и формата JSON-тела
        response.assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
}