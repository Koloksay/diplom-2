import client.ClientClient;
import client.RegisterClient;
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

public class CreateClientTest {
    RegisterClient registerClient;
    ClientClient clientClient;
    ClientData client;
    String bearerToken;

    @Before
    public void setup() {
        registerClient = new RegisterClient();
        clientClient = new ClientClient();
    }

    @After
    //если token isNull не удаляем, если не Null - удаляем
    public void cleanUp() {
        if (bearerToken != null) {
            clientClient.deleteClient(bearerToken);
        }
    }

    @DisplayName("Можно создать клиента. Код ответа сервера 200 ОК")
    @Test
    public void ClientCanBeCreated() {
        client = ClientGenerator.getRandomClient();
        ValidatableResponse response = registerClient.createNewClient(client);

        // Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_OK);

       // Проверка формата JSON-тела
        response.assertThat()
                .body("success", is(true))
                .body("user.email", is(client.getEmail().toLowerCase()))
                .body("user.name", is(client.getName()))
                .body("accessToken", isA(String.class))
                .body("refreshToken", isA(String.class));

        // Получение значения AccessToken для последующего удаления клиента
        bearerToken = response.extract().path("accessToken");

    }

    @DisplayName("Нельзя создать не уникального клиента. Код ответа сервера 403 Forbidden")
    @Test
    public void ClientAnotherOneCantBeCreated() {
        // Создание клиента
        client = ClientGenerator.getRandomClient();
        ValidatableResponse response = registerClient.createNewClient(client);

        // Проверка статуса ответа при первой попытке создания клиента
        response.assertThat()
                .statusCode(HttpStatus.SC_OK);

        // Получение accessToken
        bearerToken = response.extract().path("accessToken");

        // Попытка создания клиента с теми же самыми данными
        ValidatableResponse secondResponse = registerClient.createNewClient(client);

        // Проверка статуса ответа
        secondResponse.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        // Проверка формата JSON-тела
        secondResponse.assertThat()
                .body("success", is(false))
                .body("message", is("User already exists"));
    }

    @DisplayName("Нельзя создать клиента, если не перадать значение 'Name'. Код ответа сервера 403 Forbidden")
    @Test
    public void ClientCantBeCreatedWithoutName() {
        client = ClientGenerator.getRandomCourierWithoutName();
        ValidatableResponse response = registerClient.createNewClient(client);

        /// Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        // Проверка формата JSON-тела
        response.assertThat()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @DisplayName("Нельзя создать клиента, если не перадать значение 'Password'. Код ответа сервера 403 Forbidden")
    @Test
    public void ClientCantBeCreatedWithoutPassword() {
        client = ClientGenerator.getRandomCourierWithoutPassword();
        ValidatableResponse response = registerClient.createNewClient(client);

        /// Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        // Проверка формата JSON-тела
        response.assertThat()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @DisplayName("Нельзя создать клиента, если не перадать значение Email")
    @Test
    public void ClientCantBeCreatedWithoutEmail() {
        client = ClientGenerator.getRandomCourierWithoutEmail();
            ValidatableResponse response = registerClient.createNewClient(client);

        /// Проверка статуса ответа
        response.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        // Проверка формата JSON-тела
        response.assertThat()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }
}