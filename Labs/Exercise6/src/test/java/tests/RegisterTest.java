package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.RegisterPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Register Tests - Template Driven")
public class RegisterTest extends BaseTest {

    private static WebDriverWait wait;
    private static RegisterPage registerPage;

    @BeforeAll
    static void initPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        registerPage = new RegisterPage(driver);
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/register-data.csv", numLinesToSkip = 1)
    @DisplayName("Register scenarios from standardized CSV")
    void testRegisterFromTemplateCsv(String testCaseId,
                                     String testCaseDescription,
                                     String preConditions,
                                     String testCaseProcedure,
                                     String testData,
                                     String expectedResults,
                                     String priority,
                                     String severity,
                                     String status,
                                     String username,
                                     String email,
                                     String fullName,
                                     String password,
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

        registerPage.navigate();

        String resolvedUsername = resolveEmptyToken(username);
        String resolvedEmail = resolveEmptyToken(email);
        String resolvedFullName = resolveEmptyToken(fullName);
        String resolvedPassword = resolveEmptyToken(password);
        String resolvedConfirmPassword = resolveEmptyToken(confirmPassword);
        String resolvedExpected = clean(automationExpected);

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            String suffix = String.valueOf(System.currentTimeMillis());
            String usernamePrefix = resolvedUsername.isEmpty() ? "autouser" : resolvedUsername;
            resolvedUsername = usernamePrefix + suffix;
            resolvedEmail = resolvedUsername + "@example.com";
        }

        registerPage.fillRegistrationForm(
            resolvedUsername,
            resolvedEmail,
            resolvedFullName,
            resolvedPassword,
            resolvedConfirmPassword
        );
        registerPage.submitRegistration();

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            wait.until(ExpectedConditions.urlContains("/Account/Login"));
            assertTrue(registerPage.isRedirectedToLogin(),
                caseMessage(meta, "Successful registration must redirect to /Account/Login"));

            WebElement successAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registerPage.getSuccessAlertLocator()));
            assertTrue(successAlert.isDisplayed(),
                caseMessage(meta, "Success alert must be visible after successful registration"));
            return;
        }

        if (resolvedUsername.isEmpty() || resolvedEmail.isEmpty()) {
            assertTrue(registerPage.isOnRegisterPage(),
                caseMessage(meta, "Missing required fields must keep user on Register page"));
            return;
        }

        boolean hasValidationOrServerError =
            !driver.findElements(registerPage.getUsernameValidationLocator()).isEmpty()
            || !driver.findElements(registerPage.getEmailValidationLocator()).isEmpty()
            || !driver.findElements(registerPage.getPasswordValidationLocator()).isEmpty()
            || !driver.findElements(registerPage.getConfirmPasswordValidationLocator()).isEmpty()
            || !driver.findElements(registerPage.getErrorAlertLocator()).isEmpty();

        assertTrue(hasValidationOrServerError,
            caseMessage(meta, "Invalid registration input must show validation or server error"));
        assertTrue(registerPage.isOnRegisterPage(),
            caseMessage(meta, "Invalid registration input must keep user on Register page"));
    }
}