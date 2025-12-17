package org.rodut.playwright.ui.toolshop.fixtures;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public abstract class PlaywrightTestCase {
        protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        return playwright;
    });
    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(true)
                    .setArgs(Arrays.asList("--no-sanxbox", "--disable-extension", "--disable-gpu"))));
    protected BrowserContext browserContext;
    protected Page page;

    @BeforeEach
    void setUp() {
        browserContext = browser.get().newContext();
        page = browserContext.newPage();
        page.navigate("https://practicesoftwaretesting.com");
    }

    @AfterEach
    void tearDown() {
        ScreenshotManager.takeScreenshot(page, "End of test");
        browserContext.close();
    }

    @AfterAll
    static void tearDownClass() {
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }
}
