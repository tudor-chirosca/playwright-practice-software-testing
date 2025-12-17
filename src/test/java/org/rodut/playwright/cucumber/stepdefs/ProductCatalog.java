package org.rodut.playwright.cucumber.stepdefs;

import org.rodut.playwright.ui.toolshop.domain.ProductSummary;
import org.rodut.playwright.ui.toolshop.pageobjects.NavBar;
import org.rodut.playwright.ui.toolshop.pageobjects.ProductList;
import org.rodut.playwright.ui.toolshop.pageobjects.SearchComponent;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

public class ProductCatalog {
    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;

    @Before(order = 2)
    public void setUpPageObjects() {
        navBar = new NavBar();
        searchComponent = new SearchComponent();
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("user is on the home page")
    public void navigateToHomePage() {
        navBar.goToHomePage();
    }

    @When("user searches for {string}")
    public void searchForProduct(String productName) {
        searchComponent.searchBy(productName);
    }

    @Then("{string} should be displayed")
    public void verifyProductIsDisplayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);
    }

    @Then("the following products should be displayed")
    public void verifyProductsAreDisplayed(List<String> expectedProducts) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).containsAll(expectedProducts);
    }

//    @And("the following products are displayed in table")
//    public void verifyProductsAreDisplayedInTable(DataTable expectedProducts) {
//        var matchingProducts = productList.getProductSummaries();
//        List<Map<String, String>> list = expectedProducts.asMaps();
//        List<ProductSummary> expectedProductSummaries = list.stream()
//                .map(productData -> new ProductSummary(productData.get("Product"), productData.get("Price"))).toList();
//        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);
//    }

    /**
     * OR it can be written in a fewer lines of code
     */

    @DataTableType
    public ProductSummary getProductSummaryRow(Map<String, String> map) {
        return new ProductSummary(map.get("Product"), map.get("Price"));
    }

    @And("the following products are displayed in table")
    public void verifyProductsAreDisplayedInTable(List<ProductSummary> expectedProductSummaries) {
        var matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);
    }

    @Then("no product should be displayed")
    public void verifyProductShouldNotBeDisplayed() {
        var matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).isEmpty();
    }

    @And("the text {string} is displayed")
    public void verifyTextIsDisplayed(String text) {
        String message = productList.getSearchCompletedMessage();
        Assertions.assertThat(message).isEqualTo(text);
    }

    @And("user filters by {string} category")
    public void filterByCategory(String filterName) {
        searchComponent.filterBy(filterName);
    }

    @When("user sorts by {string}")
    public void sortBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
    }

    @Then("the first product displayed should be {string}")
    public void verifyFirstProductDisplayedShouldBe(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).startsWith(productName);
    }
}
