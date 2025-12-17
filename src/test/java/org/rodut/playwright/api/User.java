package org.rodut.playwright.api;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 {
 "first_name": "John",
 "last_name": "Doe",
 "address": {
 "street": "Street 1",
 "city": "City",
 "state": "State",
 "country": "Country",
 "postal_code": "1234AA"
 },
 "phone": "0987654321",
 "dob": "1970-01-01",
 "password": "SuperSecure@123",
 "email": "john@doe.example"
 }
 */
public record User(String first_name,
                   String last_name,
                   Address address,
                   String phone,
                   String dob,
                   String password,
                   String email) {

    public static User createRandomUser() {
        Faker fake = new Faker();
        int year = fake.number().numberBetween(1970, 2000);
        int month = fake.number().numberBetween(1, 12);
        int day = fake.number().numberBetween(1, 28);
        LocalDate date = LocalDate.of(year, month, day);
        String formatedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Address address = new Address(
                fake.address().streetAddress(),
                fake.address().city(),
                fake.address().state(),
                fake.address().country(),
                fake.address().postcode()
        );
        return new User(
                fake.name().firstName(),
                fake.name().lastName(),
                address,
                fake.phoneNumber().phoneNumber(),
                formatedDate,
                "Az123!&xyz",
                fake.internet().emailAddress());
    }

    public User getPassword(String password) {
        return new User(first_name, last_name, address, phone, dob, password, email);
    }
}
