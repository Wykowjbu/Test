package huyde180519.exercise5.tests;

import huyde180519.exercise5.pages.PracticeFormPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Practice Form Tests - demoqa.com")
public class PracticeFormTest extends BaseTest {

    static PracticeFormPage formPage;
    static WebDriverWait wait;

    @BeforeAll
    static void initPage() {
        formPage = new PracticeFormPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    @Order(1)
    @DisplayName("Should submit form successfully with all valid data")
    void testSubmitFormSuccess() {
        formPage.navigate();

        formPage.fillForm(
                "Huy",
                "Nguyen",
                "huy@example.com",
                "Male",
                "0123456789",
                "Maths",
                "Sports",
                "123 Main Street, Hanoi"
        );

        formPage.enterDateOfBirth("15", "January", "2000");
        formPage.selectState("NCR");
        formPage.selectCity("Delhi");
        formPage.clickSubmit();

        // Verify the modal appears with success title
        assertTrue(formPage.isSubmissionSuccessful(), "Submission modal should appear");
        assertEquals("Thanks for submitting the form", formPage.getModalTitle());

        // Verify submitted data in modal body
        String modalBody = formPage.getModalBodyText();
        assertTrue(modalBody.contains("Huy"), "Modal should contain first name");
        assertTrue(modalBody.contains("Nguyen"), "Modal should contain last name");
        assertTrue(modalBody.contains("huy@example.com"), "Modal should contain email");
        assertTrue(modalBody.contains("Male"), "Modal should contain gender");
        assertTrue(modalBody.contains("0123456789"), "Modal should contain mobile");
        assertTrue(modalBody.contains("Maths"), "Modal should contain subject");
        assertTrue(modalBody.contains("Sports"), "Modal should contain hobby");
        assertTrue(modalBody.contains("123 Main Street, Hanoi"), "Modal should contain address");
        assertTrue(modalBody.contains("NCR"), "Modal should contain state");
        assertTrue(modalBody.contains("Delhi"), "Modal should contain city");

        formPage.closeModal();
    }

    @Test
    @Order(2)
    @DisplayName("Should submit form with only required fields (Name, Gender, Mobile)")
    void testSubmitFormWithRequiredFieldsOnly() {
        formPage.navigate();

        formPage.enterFirstName("An");
        formPage.enterLastName("Tran");
        formPage.selectGender("Female");
        formPage.enterMobile("0987654321");
        formPage.clickSubmit();

        assertTrue(formPage.isSubmissionSuccessful(), "Submission modal should appear");
        assertEquals("Thanks for submitting the form", formPage.getModalTitle());

        String modalBody = formPage.getModalBodyText();
        assertTrue(modalBody.contains("An"), "Modal should contain first name");
        assertTrue(modalBody.contains("Tran"), "Modal should contain last name");
        assertTrue(modalBody.contains("Female"), "Modal should contain gender");
        assertTrue(modalBody.contains("0987654321"), "Modal should contain mobile");

        formPage.closeModal();
    }

    @Test
    @Order(3)
    @DisplayName("Should NOT submit form when required fields are empty")
    void testSubmitFormWithEmptyRequiredFields() {
        formPage.navigate();

        // Submit without filling anything
        formPage.clickSubmit();

        // The modal should NOT appear
        assertFalse(formPage.isSubmissionSuccessful(),
                "Submission modal should NOT appear when required fields are empty");
    }

    @Test
    @Order(4)
    @DisplayName("Should NOT submit form when mobile number is less than 10 digits")
    void testSubmitFormWithInvalidMobile() {
        formPage.navigate();

        formPage.enterFirstName("Test");
        formPage.enterLastName("User");
        formPage.selectGender("Male");
        formPage.enterMobile("12345"); // Invalid: less than 10 digits
        formPage.clickSubmit();

        // The modal should NOT appear because mobile is invalid
        assertFalse(formPage.isSubmissionSuccessful(),
                "Submission modal should NOT appear with invalid mobile");
    }

    @Test
    @Order(5)
    @DisplayName("Should NOT submit form when email format is invalid")
    void testSubmitFormWithInvalidEmail() {
        formPage.navigate();

        formPage.enterFirstName("Test");
        formPage.enterLastName("User");
        formPage.enterEmail("invalid-email");
        formPage.selectGender("Other");
        formPage.enterMobile("0123456789");
        formPage.clickSubmit();

        // The modal should NOT appear because email format is invalid
        assertFalse(formPage.isSubmissionSuccessful(),
                "Submission modal should NOT appear with invalid email");
    }

    @ParameterizedTest(name = "Inline CSV: {0} {1} - {5}")
    @Order(6)
    @CsvSource({
            "Huy, Nguyen, huy@test.com, Male, 0123456789, success",
            "An, Tran, an@test.com, Female, 0987654321, success",
            "'', '', '', Male, 0123456789, fail",
            "Test, User, test@test.com, Male, 123, fail",
            "Test, User, invalid-email, Female, 0123456789, fail"
    })
    @DisplayName("Parameterized test with CsvSource")
    void testFormWithCsvSource(String firstName, String lastName, String email,
                               String gender, String mobile, String expectedResult) {
        formPage.navigate();

        firstName = (firstName == null) ? "" : firstName.trim();
        lastName = (lastName == null) ? "" : lastName.trim();
        email = (email == null) ? "" : email.trim();
        mobile = (mobile == null) ? "" : mobile.trim();

        if (!firstName.isEmpty()) formPage.enterFirstName(firstName);
        if (!lastName.isEmpty()) formPage.enterLastName(lastName);
        if (!email.isEmpty()) formPage.enterEmail(email);
        formPage.selectGender(gender.trim());
        if (!mobile.isEmpty()) formPage.enterMobile(mobile);

        formPage.clickSubmit();

        if (expectedResult.trim().equals("success")) {
            assertTrue(formPage.isSubmissionSuccessful(),
                    "Submission should succeed for: " + firstName + " " + lastName);
            assertEquals("Thanks for submitting the form", formPage.getModalTitle());
            formPage.closeModal();
        } else {
            assertFalse(formPage.isSubmissionSuccessful(),
                    "Submission should fail for: " + firstName + " " + lastName);
        }
    }

    @ParameterizedTest(name = "CSV File: {0} {1} - {5}")
    @Order(7)
    @CsvFileSource(resources = "/form-data.csv", numLinesToSkip = 1)
    @DisplayName("Parameterized test with external CSV file")
    void testFormWithCsvFile(String firstName, String lastName, String email,
                             String gender, String mobile, String expectedResult) {
        formPage.navigate();

        firstName = (firstName == null) ? "" : firstName.trim();
        lastName = (lastName == null) ? "" : lastName.trim();
        email = (email == null) ? "" : email.trim();
        mobile = (mobile == null) ? "" : mobile.trim();

        if (!firstName.isEmpty()) formPage.enterFirstName(firstName);
        if (!lastName.isEmpty()) formPage.enterLastName(lastName);
        if (!email.isEmpty()) formPage.enterEmail(email);
        formPage.selectGender(gender.trim());
        if (!mobile.isEmpty()) formPage.enterMobile(mobile);

        formPage.clickSubmit();

        if (expectedResult.trim().equals("success")) {
            assertTrue(formPage.isSubmissionSuccessful(),
                    "Submission should succeed for: " + firstName + " " + lastName);
            assertEquals("Thanks for submitting the form", formPage.getModalTitle());
            formPage.closeModal();
        } else {
            assertFalse(formPage.isSubmissionSuccessful(),
                    "Submission should fail for: " + firstName + " " + lastName);
        }
    }
}
