package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import pages.LoginPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Login Tests using Page Object Model")
public class LoginTest extends BaseTest {
    static WebDriverWait wait;
    static LoginPage loginPage;

    @BeforeAll
    static void initPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Should login successfully with valid credentials")
    void testLoginSuccess() {
        loginPage.navigate();
        loginPage.login("phanhuy", "1234567");
        // After successful login, user is redirected to home page
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
        assertTrue(loginPage.isLoginSuccessful(), "Login should redirect away from login page");
    }

    @Test
    @Order(2)
    @DisplayName("Should show error for invalid credentials")
    void testLoginFail() {
        loginPage.navigate();
        loginPage.login("wronguser", "wrongpassword");
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getErrorLocator()));
        assertTrue(error.getText().toLowerCase().contains("invalid"), "Error message should contain 'invalid'");
    }

    @Test
    @Order(3)
    @DisplayName("Should show validation errors for empty credentials")
    void testLoginEmptyCredentials() {
        loginPage.navigate();
        loginPage.login("", "");
        // HTML5 required validation prevents form submission, user stays on login page
        assertTrue(loginPage.isOnLoginPage(), "User should remain on login page with empty credentials");
    }

    @ParameterizedTest(name = "CSV Inline: {0} / {1} => {2}")
    @Order(4)
    @CsvSource({
            "phanhuy, 1234567, success",
            "wronguser, ValidPassword123!, error",
            "validUser@example.com, wrongpassword, error",
            "EMPTY, EMPTY, error"
    })
    @DisplayName("Parameterized Login Test with inline CSV")
    void testLoginCsvInline(String username, String password, String expected) {
        loginPage.navigate();
        if ("EMPTY".equals(username)) username = "";
        if ("EMPTY".equals(password)) password = "";
        username = (username == null) ? "" : username.trim();
        password = (password == null) ? "" : password.trim();

        loginPage.login(username, password);

        if (expected.equals("success")) {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
            assertTrue(loginPage.isLoginSuccessful(), "Login should redirect away from login page");
        } else if (username.isEmpty() || password.isEmpty()) {
            // HTML5 validation prevents submit, user stays on login page
            assertTrue(loginPage.isOnLoginPage(), "User should remain on login page with empty fields");
        } else {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getErrorLocator()));
            assertTrue(error.getText().toLowerCase().contains("invalid")
                    || error.isDisplayed(), "Error message should be displayed for invalid credentials");
        }
    }

    @ParameterizedTest(name = "CSV File: {0} / {1} => {2}")
    @Order(5)
    @CsvFileSource(resources = "/login-data.csv", numLinesToSkip = 1)
    @DisplayName("Parameterized Login Test from CSV file")
    void testLoginFromCSV(String username, String password, String expected) {
        loginPage.navigate();
        username = (username == null) ? "" : username.trim();
        password = (password == null) ? "" : password.trim();

        loginPage.login(username, password);

        if (expected.equals("success")) {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
            assertTrue(loginPage.isLoginSuccessful(), "Login should redirect away from login page");
        } else if (username.isEmpty() || password.isEmpty()) {
            assertTrue(loginPage.isOnLoginPage(), "User should remain on login page with empty fields");
        } else {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getErrorLocator()));
            assertTrue(error.getText().toLowerCase().contains("invalid")
                    || error.isDisplayed(), "Error message should be displayed for invalid credentials");
        }
    }
}
