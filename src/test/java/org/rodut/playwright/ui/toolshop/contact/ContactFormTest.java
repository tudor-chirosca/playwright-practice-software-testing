package org.rodut.playwright.ui.toolshop.contact;

import com.microsoft.playwright.Page;
import org.rodut.playwright.cucumber.stepdefs.PlaywrightCucumberFixtures;
import org.rodut.playwright.ui.toolshop.pageobjects.ContactForm;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Feature("Contact Form")
public class ContactFormTest {
    ContactForm contactForm;
    Page page = PlaywrightCucumberFixtures.getPage();

    @BeforeEach
    void openContactPage() {
        contactForm = new ContactForm(page);
        page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @Test
    @DisplayName("Use the contact form")
    void useContactForm() throws URISyntaxException {
        contactForm.setFirstName("Sarah");
        contactForm.setLastName("Smith");
        contactForm.setEmailField("sarah.smith@example.com");
        contactForm.setMessageField("Dolor ipse vitandus est, sed interdum accipiendus est. Labor ac studium saepe fructus ferunt, et magna ex deditione oriri possunt.");
        contactForm.selectSubject("Warranty");
        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        contactForm.setAttachment(fileToUpload);
        contactForm.submitForm();
        Assertions.assertThat(contactForm.getAlertMessage()).isEqualTo("Thanks for your message! We will contact you shortly.");
    }
}
