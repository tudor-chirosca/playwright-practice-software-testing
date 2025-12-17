package org.rodut.playwright.api;

public record Address(
        String street,
        String city,
        String state,
        String country,
        String postal_code) {
}