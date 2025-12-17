package org.rodut.playwright.ui.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public abstract class BasePage {
    protected final Page page;
    public String searchCaptionElement = "[data-test=search-caption]";

    protected BasePage(Page page) {
        this.page = page;
    }

    @Step("Open home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }
}
