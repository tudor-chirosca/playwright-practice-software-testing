package org.rodut.playwright.ui.toolshop.login;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rodut.playwright.api.User;
import org.rodut.playwright.api.UserAPIClient;
import org.rodut.playwright.ui.toolshop.fixtures.PlaywrightManager;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginWithRegisterUserTest {
    Page page = PlaywrightManager.createPage();

    @Test
    @DisplayName("Should be able to login with a registered user")
    void loginWithRegisteredUser() {
        User user = User.createRandomUser();
        UserAPIClient userApiClient = new UserAPIClient(page);
        userApiClient.registerUser(user);
        LoginPage loginPage = new LoginPage(page);
        loginPage.openPage();
        loginPage.loginAs(user);
        assertThat(loginPage.getTitle()).isEqualTo("My account");
    }

    @Test
    @DisplayName("Should reject user with invalid password")
    void rejectInvalidPassword() {
        User user = User.createRandomUser();
        UserAPIClient userApiClient = new UserAPIClient(page);
        userApiClient.registerUser(user);
        LoginPage loginPage = new LoginPage(page);
        loginPage.openPage();
        loginPage.loginAs(user.getPassword("wrong-password"));
        assertThat(loginPage.loginErrorMessage()).isEqualTo("Invalid email or password");
    }
}
