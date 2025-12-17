package org.rodut.playwright.ui.toolshop.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.rodut.playwright.api.User;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void openPage() {
        System.out.println("Open Login page...");
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    public void loginAs(User user) {
        page.getByPlaceholder("Your email").fill(user.email());
        page.getByPlaceholder("Your password").fill(user.password());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
    }

    public String getTitle() {
        System.out.println("Get page title...");
        return page.getByTestId("page-title").textContent();
    }

    public String loginErrorMessage() {
        return page.getByTestId("login-error").textContent();
    }
}
