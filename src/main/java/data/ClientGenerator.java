package data;

import org.apache.commons.lang3.RandomStringUtils;

public class ClientGenerator {

    public static ClientData getRandomClient() {
        String email = generateRandomEmail();
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new ClientData(email, password, name);
    }

    public static ClientData getRandomCourierWithoutEmail() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new ClientData(null, password, name);
    }

    public static ClientData getRandomCourierWithoutPassword() {
        String email = generateRandomEmail();
        String name = RandomStringUtils.randomAlphabetic(10);

        return new ClientData(email, null, name);
    }

    public static ClientData getRandomCourierWithoutName() {
        String email = generateRandomEmail();
        String password = RandomStringUtils.randomAlphabetic(10);

        return new ClientData(email, password, null);
    }


    public static String generateRandomEmail() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        return String.format("%s@gmail.com", randomString);
    }
}