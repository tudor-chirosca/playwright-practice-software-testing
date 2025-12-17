package org.rodut.playwright.ui.toolshop.pageobjects;

import com.microsoft.playwright.Page;

import java.util.List;

public class CheckoutCart extends BasePage {
    public CheckoutCart(Page page) {
        super(page);
    }

    public List<CartLineItem> getLineItems() {
        page.locator("app-cart tbody tr").first().waitFor();
        return page.locator("app-cart tbody tr")
                .all()
                .stream()
                .map(
                        row -> {
                            String title = trimmed(row.getByTestId("product-title").innerText());
                            int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                            double price = Double.parseDouble(getPrice(row.getByTestId("product-price").innerText()));
                            double linePrice = Double.parseDouble(getPrice(row.getByTestId("line-price").innerText()));
                            return new CartLineItem(title, quantity, price, linePrice);
                        }
                ).toList();
    }

    private String getPrice(String value) {
        return value.replace("$", "");
    }

    private String trimmed(String value) {
        return value.strip().replaceAll("\u00A0", "");
    }

}
