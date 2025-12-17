package org.rodut.playwright.ui.toolshop.shlack;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@UsePlaywright(AnnotatedPlaywrightTest.MyOptions.class)
public class AnnotatedPlaywrightTest {
    public static class MyOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(true)
                    .setLaunchOptions(new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList("--no-sanxbox", "--disable-extension", "--disable-gpu")));
        }
    }

    @Test
    void getPageTitle(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        String pageTitle = page.title();
        Assertions.assertTrue(pageTitle.contains("Practice Software Testing"));
    }

    @Test
    void searchByKeyword(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchSearchResults > 0);
    }
}
