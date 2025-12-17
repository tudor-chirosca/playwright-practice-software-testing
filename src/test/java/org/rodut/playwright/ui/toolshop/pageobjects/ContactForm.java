package org.rodut.playwright.ui.toolshop.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Path;

public class ContactForm {
    Page page;
    Locator firstNameField;
    Locator lastNameField;
    Locator emailNameField;
    Locator messageField;
    Locator subjectField;
    Locator sendButton;

    public ContactForm(Page page) {
        this.page = page;
        firstNameField = page.getByLabel("First name");
        lastNameField = page.getByLabel("Last name");
        emailNameField = page.getByLabel("Email");
        messageField = page.getByLabel("Message");
        subjectField = page.getByLabel("Subject");
        sendButton = page.getByText("Send");
    }

    public void setFirstName(String firstName) {
        firstNameField.fill(firstName);
    }

    public void setLastName(String lastName) {
        lastNameField.fill(lastName);
    }

    public void setEmailField(String email) {
        emailNameField.fill(email);
    }

    public void setMessageField(String message) {
        messageField.fill(message);
    }

    public void selectSubject(String subject) {
        subjectField.selectOption(subject);
    }

    public void setAttachment(Path fileToUpload) {
        page.setInputFiles("#attachment", fileToUpload);
    }

    public void submitForm() {
        sendButton.click();
    }

    public String getAlertMessage() {
        return page.getByRole(AriaRole.ALERT).textContent().trim();
    }
}
