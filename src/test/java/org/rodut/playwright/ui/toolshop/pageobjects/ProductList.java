package org.rodut.playwright.ui.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.rodut.playwright.cucumber.stepdefs.PlaywrightCucumberFixtures;
import org.rodut.playwright.ui.toolshop.domain.ProductSummary;
import org.rodut.playwright.ui.toolshop.fixtures.ScreenshotManager;

import java.util.List;

public class ProductList {
    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    public List<String> getProductNames() {
        return PlaywrightCucumberFixtures.getPage().getByTestId("product-name").allInnerTexts();
    }

    @Step("View product details")
    public void viewProductDetails(String productName) {
        PlaywrightCucumberFixtures.getPage().locator(".card").getByText(productName).click();
        ScreenshotManager.takeScreenshot(PlaywrightCucumberFixtures.getPage(), "View product details for " + productName);
    }

    public String getSearchCompletedMessage() {
        return PlaywrightCucumberFixtures.getPage().getByTestId("search_completed").textContent();
    }

    /**
     * This method collects the name and price of the products displayed on the given page
     */
    public List<ProductSummary> getProductSummaries() {
        return PlaywrightCucumberFixtures.getPage().locator(".card").all().stream().map(productCard -> {
            String productName = productCard.getByTestId("product-name").textContent().trim();
            String productPrice = productCard.getByTestId("product-price").textContent();
            return new ProductSummary(productName, productPrice);
        }).toList();
    }
}
