package order;

import client.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestClient {
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Send GET request to /api/ingredients")
    public List<String> getAllIngredientsIds() {
        String response = given()
                .spec(requestSpecification())
                .get(INGREDIENTS_PATH)
                .asString();

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(response, JsonElement.class);
        JsonArray dataArray = jsonElement.getAsJsonObject().getAsJsonArray("data");

        // Создаем список для хранения значений "_id"
        List<String> ingredientIds = new ArrayList<>();

        for (JsonElement element : dataArray) {
            String ingredientId = element.getAsJsonObject().get("_id").getAsString();
            ingredientIds.add(ingredientId);
        }

        return ingredientIds;
    }
}