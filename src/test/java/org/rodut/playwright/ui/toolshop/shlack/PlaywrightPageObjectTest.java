package org.rodut.playwright.ui.toolshop.shlack;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.rodut.playwright.cucumber.stepdefs.PlaywrightCucumberFixtures;
import org.rodut.playwright.ui.toolshop.fixtures.PlaywrightTestCase;
import org.rodut.playwright.ui.toolshop.pageobjects.ProductList;
import org.rodut.playwright.ui.toolshop.pageobjects.SearchComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaywrightPageObjectTest extends PlaywrightTestCase {
    Page page = PlaywrightCucumberFixtures.getPage();

//    @BeforeEach
//    void setUp() {
//        page.navigate("https://practicesoftwaretesting.com");
//    }

//    @DisplayName("Without Page Objects")
//    @Test
//    void doWithoutPageObjects() {
//        page.waitForResponse("**/products/search?q=tape", () -> {
//            page.getByPlaceholder("Search").fill("tape");
//            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
//        });
//        List<String> matchingProducts = page.getByTestId("product-name").allInnerTexts();
//        assertThat(matchingProducts).contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
//    }
//
//    @DisplayName("With Page Objects")
//    @Test
//    void doWithPageObjects() {
//        SearchComponent searchComponent = new SearchComponent(page);
//        ProductList productList = new ProductList(page);
//        searchComponent.searchBy("tape");
//        var matchingProducts = productList.getProductNames();
//        assertThat(matchingProducts).contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
//    }
}
