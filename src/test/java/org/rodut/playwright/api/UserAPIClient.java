package org.rodut.playwright.api;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;

public class UserAPIClient {
    private final Page page;
    private static final String REGISTER_USER_URL = "https://api.practicesoftwaretesting.com/users/register";

    public UserAPIClient(Page page){
        this.page = page;
    }

    public void registerUser(User user){
        System.out.println("Registering new user...");
        var response = page.request().post(REGISTER_USER_URL,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setData(user));
        if (response.status() != 201){
            throw new IllegalArgumentException("Could not create user: " + response.text());
        }
    }
}
