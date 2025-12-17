package org.rodut.playwright.ui.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.rodut.playwright.ui.toolshop.fixtures.ScreenshotManager;

public class ProductDetails extends BasePage {
    public ProductDetails(Page page) {
        super(page);
    }

    @Step("Increase quantity")
    public void increaseQuantityBy(int increment) {
        for (int i = 1; i <= increment; i++) {
            page.getByTestId("increase-quantity").click();
        }
        ScreenshotManager.takeScreenshot(page, "Quantity increased by " + increment + " unit(s)");
    }

    @Step("Add to cart")
    public void addToCart() {
        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () -> page.getByText("Add to cart").click()
        );
        ScreenshotManager.takeScreenshot(page, "Added to cart");
    }
}
