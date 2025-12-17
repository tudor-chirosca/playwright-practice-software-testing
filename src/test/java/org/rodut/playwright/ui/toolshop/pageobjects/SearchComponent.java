package org.rodut.playwright.ui.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.rodut.playwright.cucumber.stepdefs.PlaywrightCucumberFixtures;

public class SearchComponent {
    @Step("Search for keyword")
    public void searchBy(String keyword) {
        PlaywrightCucumberFixtures.getPage().waitForResponse("**/products/search?**", () -> {
            PlaywrightCucumberFixtures.getPage().getByPlaceholder("Search").fill(keyword);
            PlaywrightCucumberFixtures.getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
    }

    public void viewProductDetails(String productName) {
        PlaywrightCucumberFixtures.getPage().locator(".card").getByText(productName).click();
    }

    @Step("Clear the search criteria")
    public void clearSearch() {
        PlaywrightCucumberFixtures.getPage().waitForResponse("**/products**", () -> {
            PlaywrightCucumberFixtures.getPage().getByTestId("search-reset").click();
        });
    }

    public void filterBy(String filterName) {
        PlaywrightCucumberFixtures.getPage().waitForResponse("**/products**by_category=**", () -> {
            PlaywrightCucumberFixtures.getPage().getByLabel(filterName).click();
        });

    }

    public void sortBy(String sortFilter) {
        PlaywrightCucumberFixtures.getPage().waitForResponse("**/products**sort**", () -> {
            PlaywrightCucumberFixtures.getPage().getByTestId("sort").selectOption(sortFilter);
        });
    }
}
