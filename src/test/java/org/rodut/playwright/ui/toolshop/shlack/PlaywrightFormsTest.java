package org.rodut.playwright.ui.toolshop.shlack;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

//@UsePlaywright(HeadlessChromeOptions.class)
@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightFormsTest {

    @DisplayName("Interacting with text fields")
    @Nested
    class InteractingWithTextFields {

        private Page page;

        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Fill the form")
        @Test
        void fillUserDetailsForm() throws URISyntaxException {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("Sarah");
            lastNameField.fill("Smith");
            emailField.fill("sarah.smith@example.com");
            messageField.fill("Hi there!");
            subjectField.selectOption("Warranty");
//            subjectField.selectOption(new SelectOption().setIndex(2)); // Test will fail because the second index value is "Webmaster"

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
            page.setInputFiles("#attachment", fileToUpload);

            assertThat(firstNameField).hasValue("Sarah");
            assertThat(lastNameField).hasValue("Smith");
            assertThat(emailField).hasValue("sarah.smith@example.com");
            assertThat(messageField).hasValue("Hi there!");
            assertThat(subjectField).hasValue("warranty");

            String uploadedFile = uploadField.inputValue();
            Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");

        }

        @DisplayName("Check for mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        void checkMandatoryFields(String fieldName){
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");

            /* Fill in the field value */
            firstNameField.fill("Sarah");
            lastNameField.fill("Smith");
            emailField.fill("sarah.smith@example.com");
            messageField.fill("Hi there!");
            subjectField.selectOption("Warranty");


            /* Clear one of the fields */
            page.getByLabel(fieldName).clear();
            sendButton.click();

            /* Check the error message for that cleared field */
            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

            assertThat(errorMessage).isVisible();
        }
    }
}
