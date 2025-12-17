package org.rodut.playwright.ui.toolshop.shlack;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SimplePlaywrightTest {

    // !!! By using @UsePlaywright annotation we can skip the following declarations !!!

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;
    private static BrowserContext browserContext;

    @BeforeAll
     static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sanxbox", "--disable-extension", "--disable-gpu")));
        browserContext = browser.newContext();
    }

    @BeforeEach
    void setUp(){
        page = browserContext.newPage();
    }

    @AfterAll
     static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void getPageTitle() {
        page.navigate("https://practicesoftwaretesting.com/");
        String pageTitle = page.title();
        Assertions.assertTrue(pageTitle.contains("Practice Software Testing"));
    }

    @Test
    void searchByKeyword() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchSearchResults > 0);
    }
}
