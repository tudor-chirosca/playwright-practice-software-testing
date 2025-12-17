package org.rodut.playwright.cucumber.stepdefs;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.Arrays;

public class PlaywrightCucumberFixtures {
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    @Before(order = 1)
    public void setup() {
        Playwright pw = Playwright.create();
        pw.selectors().setTestIdAttribute("data-test");
        playwright.set(pw);
        Browser br = pw.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
        );
        browser.set(br);
        BrowserContext context = br.newContext();
        Page pg = context.newPage();
        browserContext.set(context);
        page.set(pg);
    }

    @After
    public void cleanup() {
        try {
            page.get().close();
        } catch (Exception ignored) {
        }
        try {
            browserContext.get().close();
        } catch (Exception ignored) {
        }
        try {
            browser.get().close();
        } catch (Exception ignored) {
        }
        try {
            playwright.get().close();
        } catch (Exception ignored) {
        }
    }

    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }
}
