package org.rodut.playwright.ui.toolshop.pageobjects;

import io.qameta.allure.Step;
import org.rodut.playwright.cucumber.stepdefs.PlaywrightCucumberFixtures;
import org.rodut.playwright.ui.toolshop.fixtures.ScreenshotManager;

public class NavBar {
    String pageName = "https://practicesoftwaretesting.com";

    @Step("Open the shopping cart")
    public void openCart() {
        PlaywrightCucumberFixtures.getPage().getByTestId("nav-cart").click();
        ScreenshotManager.takeScreenshot(PlaywrightCucumberFixtures.getPage(), "Shopping cart");
    }

    @Step("Open contact page")
    public void goToContactPage() {
        PlaywrightCucumberFixtures.getPage().navigate(pageName + "/contact");
        ScreenshotManager.takeScreenshot(PlaywrightCucumberFixtures.getPage(), "Contact page");
    }

    @Step("Open home page")
    public void goToHomePage() {
        PlaywrightCucumberFixtures.getPage().navigate(pageName);
        ScreenshotManager.takeScreenshot(PlaywrightCucumberFixtures.getPage(), "Home page");
    }
}
