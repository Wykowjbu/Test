package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ChangePasswordPage;
import pages.LoginPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Change Password Tests using CSV file only")
public class ChangePasswordTest extends BaseTest {

    private static LoginPage loginPage;
    private static ChangePasswordPage changePasswordPage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        changePasswordPage = new ChangePasswordPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginFirst() {
        loginPage.navigate();
        loginPage.login("phanhuy", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: current={0}, new={1}, confirm={2}, expected={3}")
    @CsvFileSource(resources = "/change-password-data.csv", numLinesToSkip = 1)
    @DisplayName("Should validate change password based on CSV data")
    void testChangePasswordFromCsv(String currentPassword, String newPassword, String confirmPassword, String expected) {
        changePasswordPage.navigate();
        changePasswordPage.changePassword(currentPassword, newPassword, confirmPassword);

        if ("success".equalsIgnoreCase(expected.trim())) {
            // Doc: success => <div class="alert alert-success">
            WebElement success = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(changePasswordPage.getSuccessAlertLocator()));
            assertTrue(success.isDisplayed(),
                    "Success alert should be displayed for valid password change");

            // Revert password to keep account stable for next test runs
            changePasswordPage.changePassword(newPassword, currentPassword, currentPassword);
            wait.until(ExpectedConditions.visibilityOfElementLocated(changePasswordPage.getSuccessAlertLocator()));
        } else {
            // Doc: mismatch -> validation rule error (.text-danger)
            // Doc: wrong old password -> error alert red (.alert-danger)
            boolean isWrongOldPassword = !"1234567".equals(currentPassword.trim());
            boolean isMismatch = !newPassword.equals(confirmPassword);

            if (isWrongOldPassword) {
                // Wrong current password: server returns .alert-danger
                WebElement error = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(changePasswordPage.getErrorAlertLocator()));
                assertTrue(error.isDisplayed(),
                        "Error alert should be shown when current password is wrong");
            } else {
                // Validation error (mismatch, too short, etc.): .text-danger
                WebElement validation = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(changePasswordPage.getValidationErrorLocator()));
                assertTrue(validation.isDisplayed(),
                        "Validation error should be shown for invalid password input");
            }

            assertTrue(changePasswordPage.isOnChangePasswordPage(),
                    "User should stay on change password page on error");
        }
    }
}