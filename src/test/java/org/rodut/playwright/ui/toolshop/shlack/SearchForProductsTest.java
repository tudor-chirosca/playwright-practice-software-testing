package org.rodut.playwright.ui.toolshop.shlack;

import io.qameta.allure.Feature;

@Feature("Search Product")
public class SearchForProductsTest {

//    Page page = PlaywrightCucumberFixtures.getPage();
//
//    @Test
//    @DisplayName("Search with no match")
//    void searchNoMatch() {
//        SearchComponent searchComponent = new SearchComponent(page);
//        ProductList productList = new ProductList(page);
//        searchComponent.searchBy("unknown");
//        page.waitForSelector("[data-test=search-caption]");
//        var matchingProducts = productList.getProductNames();
//        Assertions.assertThat(matchingProducts).isEmpty();
//        Assertions.assertThat(productList.getSearchCompletedMessage()).isEqualTo("There are no products found.");
//    }

//    @Test
//    @DisplayName("Clear the search results")
//    void clearSearchResults() {
//        SearchComponent searchComponent = new SearchComponent(page);
//        ProductList productList = new ProductList(page);
//        page.waitForSelector("[class=card-img-top]");
//        var matchingFilteredProducts = productList.getProductNames();
//        Assertions.assertThat(matchingFilteredProducts).isNotEmpty();
//        searchComponent.searchBy("unknown");
//        var matchingProducts = productList.getProductNames();
//        page.waitForSelector("[data-test=search-caption]");
//        Assertions.assertThat(matchingProducts).isEmpty();
//        Assertions.assertThat(productList.getSearchCompletedMessage()).isEqualTo("There are no products found.");
//        searchComponent.clearSearch();
//        var matchingClearedProducts = productList.getProductNames();
//        Assertions.assertThat(matchingClearedProducts).isNotEmpty();
//    }
}
