package org.rodut.playwright.ui.toolshop.catalog;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import org.rodut.playwright.cucumber.stepdefs.PlaywrightCucumberFixtures;
import org.rodut.playwright.ui.toolshop.fixtures.PlaywrightTestCase;
import org.rodut.playwright.ui.toolshop.pageobjects.CartLineItem;
import org.rodut.playwright.ui.toolshop.pageobjects.CheckoutCart;
import org.rodut.playwright.ui.toolshop.pageobjects.NavBar;
import org.rodut.playwright.ui.toolshop.pageobjects.ProductDetails;
import org.rodut.playwright.ui.toolshop.pageobjects.ProductList;
import org.rodut.playwright.ui.toolshop.pageobjects.SearchComponent;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Paths;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Shopping Cart")
@Feature("Shopping Cart")
public class AddItemToCartTest extends PlaywrightTestCase {
    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;
    Page page = PlaywrightCucumberFixtures.getPage();
    BrowserContext browserContext = PlaywrightCucumberFixtures.getBrowserContext();

//    @BeforeEach
//    void openHomePage() {
//        navBar.openHomePage();
//    }
//
//    @BeforeEach
//    void setup() {
//        searchComponent = new SearchComponent(page);
//        productList = new ProductList(page);
//        productDetails = new ProductDetails(page);
//        navBar = new NavBar(page);
//        checkoutCart = new CheckoutCart(page);
//    }

    @BeforeEach
    void setupTrace() {
        browserContext.tracing().start(
                new Tracing
                        .StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @AfterEach
    void recordTrace(TestInfo info) {
        String traceName = info.getDisplayName().replace(" ", "-").toLowerCase();
        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/traces/trace-" + traceName + ".zip"))
        );
    }

    @Test
    @DisplayName("Without Page Objects")
    void getWithoutPageObjects() {
        page.waitForResponse("**/products/search?q=pliers", () -> {
            page.getByPlaceholder("Search").fill("pliers");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
        page.locator(".card").getByText("Combination Pliers").click();
        page.getByTestId("increase-quantity").click();
        page.getByTestId("increase-quantity").click();
        page.getByText("Add to cart").click();
        page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("3"));
        page.getByTestId("nav-cart").click();
        assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
        assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();
    }

    @Test
    @DisplayName("With Page Objects")
    void doWithPageObjects() {
        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Combination Pliers");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();
        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Combination Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(3);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }

    @Test
    @Story("Checking out multiple products")
    @DisplayName("Checkout Multiple Items")
    void checkOutMultipleItems() {
        navBar.goToHomePage();
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();
        navBar.openCart();
        navBar.goToHomePage();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();
        navBar.openCart();
        List<CartLineItem> lineItems = checkoutCart.getLineItems();
        Assertions.assertThat(lineItems).hasSize(2);
        List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        Assertions.assertThat(lineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(0.1);
                    Assertions.assertThat(item.total()).isGreaterThan(0.1);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }

    @DisplayName("Search for pliers")
    @Test
    void searchForPliers() {
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        assertThat(page.locator(".card")).hasCount(4);
        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));
        Locator outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");
        assertThat(outOfStockItem).hasCount(1);
        assertThat(outOfStockItem).hasText("Long Nose Pliers");
    }
}
