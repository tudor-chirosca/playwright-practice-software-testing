package org.rodut.playwright.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.rodut.playwright.ui.toolshop.fixtures.PlaywrightManager;
import org.rodut.playwright.ui.toolshop.shlack.MockSearchResponse;

import java.util.HashMap;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.rodut.playwright.ui.toolshop.fixtures.PlaywrightManager.getPlaywright;

public class PlaywrightRestAPITest {
    BrowserContext context;
    Page page;

    @BeforeEach
    void setUp() {
        context = PlaywrightManager.createContext();
        this.page = context.newPage();
        page.navigate("https://practicesoftwaretesting.com");
    }

    @AfterEach
    void cleanUp() {
        context.close();
    }

    @AfterAll
    static void shutdown() {
        PlaywrightManager.close();
    }

    @Nested
    @DisplayName("Playwright allows us to mock out API response")
    class MockingAPIResponse {

        @Test
        @DisplayName("When search returns a single product")
        void checkSingleItemIfFound() {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponse.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200))
            );
            var searchBox = page.getByPlaceholder("Search");
            searchBox.fill("pliers");
            searchBox.press("Enter");
            assertThat(page.getByTestId("product-name")).hasCount(1);
            assertThat(page.getByTestId("product-name")).containsText("Combination Pliers");

        }

        @Test
        @DisplayName("When search returns no product")
        void checkNoItemIfFound() {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponse.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200))
            );
            var searchBox = page.getByPlaceholder("Search");
            searchBox.fill("pliers");
            searchBox.press("Enter");
            assertThat(page.getByTestId("product-name")).hasCount(0);
            assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");

        }
    }

    @Nested
    class MakingAPICalls {
        record Product(String name, Double price) {
        }

        private static APIRequestContext requestContext;

        @BeforeAll
        public static void setupRequestContext() {
            requestContext = getPlaywright().request().newContext(
                    new APIRequest.NewContextOptions()
                            .setBaseURL("https://api.practicesoftwaretesting.com")
                            .setExtraHTTPHeaders(new HashMap<>() {{
                                put("Accept", "application/json");
                            }})
            );
        }

        @DisplayName("Check presence of known products")
        @ParameterizedTest(name = "Checking product {0}")
        @MethodSource("products")
        void checkKnownProduct(Product product) {
            page.fill("[placeholder='Search']", product.name);
            /* Is equivalent to: */
//            page.getByPlaceholder("Search").fill(product.name);
            page.click("button:has-text('Search')");

            /* Check that the product appears with the correct name and price */
            Locator productCard = page.locator(".card")
                    .filter(new Locator.FilterOptions()
                            .setHasText(product.name)
                            .setHasText(Double.toString(product.price)));
            assertThat(productCard).isVisible();
        }

        static Stream<Product> products() {
            APIResponse response = requestContext.get("/products?page=2");
            Assertions.assertThat(response.status()).isEqualTo(200);
            JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);
            JsonArray data = jsonObject.getAsJsonArray("data");
            return data.asList().stream()
                    .map(jsonElement -> {
                        JsonObject productJson = jsonElement.getAsJsonObject();
                        return new Product(
                                productJson.get("name").getAsString(),
                                productJson.get("price").getAsDouble()
                        );
                    });
        }
    }
}
