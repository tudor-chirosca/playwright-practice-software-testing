package org.rodut.playwright.ui.toolshop.fixtures;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightManager {
    private static Playwright playwright;
    private static Browser browser;

    public static Playwright getPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
            playwright.selectors().setTestIdAttribute("data-test");
            System.out.println("Browser created!");
        }
        return playwright;
    }

    public static Browser getBrowser() {
        if (browser == null) {
            browser = getPlaywright()
                    .chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setSlowMo(0)
                    );
        }
        return browser;
    }

    public static BrowserContext createContext() {
        return getBrowser().newContext();
    }

    public static Page createPage() {
        return createContext().newPage();
    }

    public static void close() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
