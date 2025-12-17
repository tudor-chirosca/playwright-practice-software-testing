package org.rodut.playwright.ui.toolshop.shlack;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

//@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightWaitsTest {
    Page page;

    @BeforeEach
    void checkBeforeEach() {
        page.navigate("https://practicesoftwaretesting.com/");
    }

    @Nested
    class WaitingForAPICalls {
        @Test
        @DisplayName("Check sorting by descending price")
        void sortByDescendingPrice() {
            page.navigate("https://practicesoftwaretesting.com/");

            page.waitForResponse("**/products?page**",
                    () -> {
                        /* Sort by descending price */
                        page.getByTestId("sort").selectOption("Price (High - Low)");
                        page.getByTestId("eco-badge").first().waitFor();
                    }
            );

            /* Sort by descending price */
//            page.getByTestId("sort").selectOption("Price (High - Low)");
//            page.getByTestId("product-price").first().waitFor(); // Wait for the first product price to be visible

            /* Find all prices on the page */
            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICalls::extractPrice)
                    .toList();

            /* Check if the prices are in correct order */
            System.out.println("Product prices: " + productPrices);
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());

        }

        private static double extractPrice(String price) {
            return Double.parseDouble(price.replace("$", ""));
        }
    }

    @Nested
    class WaitingForState {

        @BeforeEach
        void checkBeforeEach() {
            page.waitForSelector("[data-test=product-name]"); // Very effective
        }

        @Test
        void checkPresenceOfAllProductNames() {
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
        }

        @Test
        void checkPresenceOfAllProductImages() {
            List<String> productImages = page.locator(".card-img-top")
                    .all()
                    .stream()
                    .map(img -> img.getAttribute("alt"))
                    .toList();

            Assertions.assertThat(productImages).contains("Pliers", "Bolt Cutters", "Hammer");

        }
    }

    @Nested
    class AutomaticWaits {

        @Test
        @DisplayName("Should wait for the filter checkbox options to appear before clicking")
        void waitForFilterCheckboxes() {
            var screwdriverFilter = page.getByLabel("Screwdriver");
            screwdriverFilter.click();
            assertThat(screwdriverFilter).isChecked();
        }

        @Test
        @DisplayName("Should filter products by category")
        void filterProductsByCategory() {
            page.getByTestId("nav-categories").click();
            page.getByTestId("nav-power-tools").click();
            assertThat(page.getByTestId("page-title")).hasText("Category: Power Tools");

            page.waitForSelector(".card", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
            var filteredProducts = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(filteredProducts).contains("Sheet Sander", "Belt Sander");
        }
    }

    @Nested
    class WaitingForElementsToAppearAndDisappear {

        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void waitDisplayToasterMessage() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            /** Wait for the toaster message to appear */
            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            /** Use an explicit wait to check if the alert disappears */
            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @Test
        @DisplayName("Should update the item card")
        void updateCartItemCount() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));

            /* Or it can be done like this */
            page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
        }
    }
}
