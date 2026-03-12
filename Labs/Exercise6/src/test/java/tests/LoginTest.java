package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Login Tests - Template Driven")
public class LoginTest extends BaseTest {

    private static WebDriverWait wait;
    private static LoginPage loginPage;

    @BeforeAll
    static void initPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/login-data.csv", numLinesToSkip = 1)
    @DisplayName("Login scenarios from standardized CSV")
    void testLoginFromTemplateCsv(String testCaseId,
                                  String testCaseDescription,
                                  String preConditions,
                                  String testCaseProcedure,
                                  String testData,
                                  String expectedResults,
                                  String priority,
                                  String severity,
                                  String status,
                                  String username,
                                  String password,
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

        loginPage.navigate();

        String resolvedUsername = resolveEmptyToken(username);
        String resolvedPassword = resolveEmptyToken(password);
        String resolvedExpected = clean(automationExpected);

        loginPage.login(resolvedUsername, resolvedPassword);

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
            assertTrue(loginPage.isLoginSuccessful(),
                caseMessage(meta, "Login success scenario must redirect away from /Account/Login"));
            return;
        }

        if (resolvedUsername.isEmpty() || resolvedPassword.isEmpty()) {
            assertTrue(loginPage.isOnLoginPage(),
                caseMessage(meta, "Empty credentials must keep user on Login page"));
            return;
        }

        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(loginPage.getErrorAlertLocator()));
        assertTrue(error.isDisplayed(),
            caseMessage(meta, "Invalid credentials must show error alert"));
    }
}