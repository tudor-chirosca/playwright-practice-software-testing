package org.rodut.playwright.ui.toolshop.shlack;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

//@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightAssertionsTest {
    Page page;

    @DisplayName("Making assertions about the contents of a field")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Checking the value of a field")
        @Test
        void checkFieldValue() {
            var firstNameField = page.getByLabel("First name");

            firstNameField.fill("Sarah");

            assertThat(firstNameField).hasValue("Sarah");
            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();
        }
    }

    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsAboutDataValues {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/");
            page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
        }

        @Test
        void checkProductPricesForCorrectValues() {
            List<Double> prices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$", "")))
                    .toList();

            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(price -> price > 0)
                    .doesNotContain(0.0)
                    .allMatch(price -> price < 1000)
                    .allSatisfy(price -> Assertions.assertThat(price).isGreaterThan(0.0).isLessThan(1000));
        }

        @Test
        void checkSortingInAlphabeticOrder(){
            page.getByLabel("sort").selectOption("Name (A - Z)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name").allTextContents();
            Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.naturalOrder());
        }

        @Test
        void checkSortingInReverseAlphabeticOrder(){
            page.getByLabel("sort").selectOption("Name (Z - A)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name").allTextContents();
            Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.reverseOrder());
        }
    }
}
