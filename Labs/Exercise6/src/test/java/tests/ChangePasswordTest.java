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

@DisplayName("Change Password Tests - Template Driven")
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
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/change-password-data.csv", numLinesToSkip = 1)
    @DisplayName("Change password scenarios from standardized CSV")
    void testChangePasswordFromTemplateCsv(String testCaseId,
                                           String testCaseDescription,
                                           String preConditions,
                                           String testCaseProcedure,
                                           String testData,
                                           String expectedResults,
                                           String priority,
                                           String severity,
                                           String status,
                                           String currentPassword,
                                           String newPassword,
                                           String confirmPassword,
                                           String automationExpected) {

        CaseMeta meta = metadata(
            testCaseId,
            testCaseDescription,
            preConditions,
            testCaseProcedure,
            testData,
            expectedResults,
            priority,
            severity,
            status
        );

        String resolvedCurrentPassword = resolveEmptyToken(currentPassword);
        String resolvedNewPassword = resolveEmptyToken(newPassword);
        String resolvedConfirmPassword = resolveEmptyToken(confirmPassword);
        String resolvedExpected = clean(automationExpected);

        changePasswordPage.navigate();
        changePasswordPage.changePassword(
            resolvedCurrentPassword,
            resolvedNewPassword,
            resolvedConfirmPassword
        );

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            WebElement success = wait.until(
                ExpectedConditions.visibilityOfElementLocated(changePasswordPage.getSuccessAlertLocator()));
            assertTrue(success.isDisplayed(),
                caseMessage(meta, "Successful password change must show success alert"));

            if (!resolvedNewPassword.isEmpty() && !resolvedNewPassword.equals(DEFAULT_PASSWORD)) {
                changePasswordPage.changePassword(resolvedNewPassword, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    changePasswordPage.getSuccessAlertLocator()));
            }
            return;
        }

        boolean isWrongCurrentPassword = !DEFAULT_PASSWORD.equals(resolvedCurrentPassword);
        if (isWrongCurrentPassword) {
            WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(changePasswordPage.getErrorAlertLocator()));
            assertTrue(error.isDisplayed(),
                caseMessage(meta, "Wrong current password must show server-side error alert"));
        } else {
            boolean hasValidationError = wait.until(webDriver ->
                !webDriver.findElements(changePasswordPage.getConfirmPasswordValidationLocator()).isEmpty()
                || !webDriver.findElements(changePasswordPage.getNewPasswordValidationLocator()).isEmpty()
            );
            assertTrue(hasValidationError,
                caseMessage(meta, "Invalid new/confirm password must show validation error"));
        }

        assertTrue(changePasswordPage.isOnChangePasswordPage(),
            caseMessage(meta, "Failure scenario must remain on Change Password page"));
    }
}