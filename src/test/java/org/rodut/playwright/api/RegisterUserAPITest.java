package org.rodut.playwright.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class RegisterUserAPITest {
    private APIRequestContext request;
    private Gson gson = new Gson();

    @BeforeEach
    void setUp(Playwright playwright) {
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    @AfterEach
    void tearDown() {
        if (request != null) {
            request.dispose();
        }
    }

    @Test
    void resisterUser() {
        User validUser = User.createRandomUser();
        System.out.println("Valid user: " + validUser);
        var response = request.post("/users/register", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(validUser));
//        assertThat(response.status()).isEqualTo(201); // Moved to soft asserts
        String responseBody = response.text();
        User createdUser = gson.fromJson(responseBody, User.class);
        System.out.println("Response: " + responseBody);
//        assertThat(createdUser).isEqualTo(validUser.getPassword(null)); // Moved to soft asserts
        JsonObject responseObj = gson.fromJson(responseBody, JsonObject.class);
        assertSoftly(softly -> {
            softly.assertThat(response.status()).as("Registration should return 201 created status code.").isEqualTo(201);
            softly.assertThat(createdUser).as("Created user should match the specified user without password.").isEqualTo(validUser.getPassword(null));
            softly.assertThat(responseObj.has("password")).as("No password should be returned.").isFalse();
            softly.assertThat(responseObj.get("id").getAsString()).as("Registered user should have an id.").isNotEmpty();
            softly.assertThat(response.headers().get("content-type")).contains("application/json");
        });
    }

    @Test
    void checkFirstNameIsMandatory() {
        User userNoName = new User(
                null,
                "Smith",
                new Address(
                        "Some street",
                        "Some city",
                        "Some state",
                        "Some country",
                        "Some postal code"),
                "12345678904",
                "1970-01-01",
                "Passw0rd!123",
                "email@email.com");
        var response = request.post("/users/register", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(userNoName));
        JsonObject responseObj = gson.fromJson(response.text(), JsonObject.class);
        String errorMessage = responseObj.get("first_name").getAsString();
        assertSoftly(softly -> {
            softly.assertThat(response.status()).as("Registration should return 422 - the first name field is required.").isEqualTo(422);
            softly.assertThat(responseObj.has("first_name")).isTrue();
            softly.assertThat(errorMessage).isEqualTo("The first name field is required.");
        });
    }
}
