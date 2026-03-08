package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import pages.RegisterPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Register Tests using Page Object Model")
public class RegisterTest extends BaseTest {

    static WebDriverWait wait;
    static RegisterPage registerPage;

    @BeforeAll
    static void initPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        registerPage = new RegisterPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Should register successfully with valid data")
    void testRegisterSuccess() {
        registerPage.navigate();
        String uniqueUser = "testuser" + System.currentTimeMillis();
        String uniqueEmail = uniqueUser + "@example.com";
        registerPage.fillRegistrationForm(uniqueUser, uniqueEmail, "Test User", "StrongP@ssw0rd!", "StrongP@ssw0rd!");
        registerPage.submitRegistration();
        // After successful registration, user is redirected to login page
        wait.until(ExpectedConditions.urlContains("/Account/Login"));
        assertTrue(registerPage.isRedirectedToLogin(), "Registration should redirect to login page");
    }

    @Test
    @Order(2)
    @DisplayName("Should show error for mismatched passwords")
    void testRegisterPasswordMismatch() {
        registerPage.navigate();
        String uniqueUser = "mismatch" + System.currentTimeMillis();
        registerPage.fillRegistrationForm(uniqueUser, uniqueUser + "@example.com", "Mismatch User", "StrongP@ssw0rd!", "DifferentPass!");
        registerPage.submitRegistration();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(registerPage.getValidationErrorLocator()));
        assertTrue(error.isDisplayed(), "Validation error should be displayed for password mismatch");
    }

    @Test
    @Order(3)
    @DisplayName("Should show error for weak password")
    void testRegisterWeakPassword() {
        registerPage.navigate();
        String uniqueUser = "weakpass" + System.currentTimeMillis();
        registerPage.fillRegistrationForm(uniqueUser, uniqueUser + "@example.com", "Weak Pass User", "123", "123");
        registerPage.submitRegistration();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(registerPage.getValidationErrorLocator()));
        assertTrue(error.isDisplayed(), "Validation error should be displayed for weak password");
    }

    @Test
    @Order(4)
    @DisplayName("Should show validation errors for empty required fields")
    void testRegisterEmptyFields() {
        registerPage.navigate();
        registerPage.fillRegistrationForm("", "", "", "", "");
        registerPage.submitRegistration();
        // HTML5 required validation prevents form submission, user stays on register page
        assertTrue(registerPage.isOnRegisterPage(), "User should remain on register page with empty fields");
    }

    @ParameterizedTest(name = "CSV Inline: {0} / {1} / {2} / {3} / {4} => {5}")
    @Order(5)
    @CsvSource({
            "validuser1, valid1@email.com, Valid User, StrongP@ss1!, StrongP@ss1!, success",
            "EMPTY, test@email.com, Test, Pass123!, Pass123!, error",
            "testuser2, EMPTY, Test, Pass123!, Pass123!, error",
            "testuser3, test3@email.com, Test, short, short, error",
            "testuser4, test4@email.com, Test, Pass123!, Mismatch!, error",
            "testuser5, invalidemail, Test, Pass123!, Pass123!, error"
    })
    @DisplayName("Parameterized Register Test with inline CSV")
    void testRegisterCsvInline(String username, String email, String fullName, String password, String confirmPassword, String expected) {
        registerPage.navigate();
        if ("EMPTY".equals(username)) username = "";
        if ("EMPTY".equals(email)) email = "";
        username = (username == null) ? "" : username.trim();
        email = (email == null) ? "" : email.trim();
        fullName = (fullName == null) ? "" : fullName.trim();
        password = (password == null) ? "" : password.trim();
        confirmPassword = (confirmPassword == null) ? "" : confirmPassword.trim();

        // Add timestamp to make username/email unique for success cases
        if (expected.trim().equals("success")) {
            String ts = String.valueOf(System.currentTimeMillis());
            username = username + ts;
            email = username + "@example.com";
        }

        registerPage.fillRegistrationForm(username, email, fullName, password, confirmPassword);
        registerPage.submitRegistration();

        if (expected.trim().equals("success")) {
            wait.until(ExpectedConditions.urlContains("/Account/Login"));
            assertTrue(registerPage.isRedirectedToLogin(), "Registration should redirect to login page");
        } else if (username.isEmpty() || email.isEmpty()) {
            // HTML5 validation prevents submit, user stays on register page
            assertTrue(registerPage.isOnRegisterPage(), "User should remain on register page with empty fields");
        } else {
            By errorLocator = registerPage.getValidationErrorLocator();
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator));
            assertTrue(result.isDisplayed(), "Error/validation message should be displayed for invalid registration data");
        }
    }

    @ParameterizedTest(name = "CSV File: {0} / {1} / {2} / {3} / {4} => {5}")
    @Order(6)
    @CsvFileSource(resources = "/register-data.csv", numLinesToSkip = 1)
    @DisplayName("Parameterized Register Test from CSV file")
    void testRegisterFromCSV(String username, String email, String fullName, String password, String confirmPassword, String expected) {
        registerPage.navigate();
        username = (username == null) ? "" : username.trim();
        email = (email == null) ? "" : email.trim();
        fullName = (fullName == null) ? "" : fullName.trim();
        password = (password == null) ? "" : password.trim();
        confirmPassword = (confirmPassword == null) ? "" : confirmPassword.trim();

        // Add timestamp to make username/email unique for success cases
        if (expected.trim().equals("success")) {
            String ts = String.valueOf(System.currentTimeMillis());
            username = username + ts;
            email = username + "@example.com";
        }

        registerPage.fillRegistrationForm(username, email, fullName, password, confirmPassword);
        registerPage.submitRegistration();

        if (expected.trim().equals("success")) {
            wait.until(ExpectedConditions.urlContains("/Account/Login"));
            assertTrue(registerPage.isRedirectedToLogin(), "Registration should redirect to login page");
        } else if (username.isEmpty() || email.isEmpty()) {
            assertTrue(registerPage.isOnRegisterPage(), "User should remain on register page with empty fields");
        } else {
            By errorLocator = registerPage.getValidationErrorLocator();
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator));
            assertTrue(result.isDisplayed(), "Error/validation message should be displayed for invalid registration data");
        }
    }
}
