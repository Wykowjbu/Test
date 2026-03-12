package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.ProfilePage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Profile Update Tests - Template Driven")
public class ProfileUpdateTest extends BaseTest {

    private static LoginPage loginPage;
    private static ProfilePage profilePage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginFirst() {
        loginPage.navigate();
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/profile-update-data.csv", numLinesToSkip = 1)
    @DisplayName("Profile update scenarios from standardized CSV")
    void testProfileUpdateFromTemplateCsv(String testCaseId,
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
                                          String bio,
                                          String dateOfBirth,
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

        profilePage.navigate();

        String resolvedUsername = resolveKeepOrEmptyToken(username, profilePage.getUsernameValue());
        String resolvedEmail = resolveKeepOrEmptyToken(email, profilePage.getEmailValue());
        String resolvedFullName = resolveKeepOrEmptyToken(fullName, profilePage.getFullNameValue());
        String resolvedBio = resolveKeepOrEmptyToken(bio, profilePage.getBioValue());
        String resolvedDateOfBirth = resolveKeepOrEmptyToken(dateOfBirth, profilePage.getDateOfBirthValue());
        String resolvedExpected = clean(automationExpected);

        profilePage.updateProfile(
            resolvedUsername,
            resolvedEmail,
            resolvedFullName,
            resolvedDateOfBirth,
            resolvedBio
        );

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            WebElement success = wait.until(
                ExpectedConditions.visibilityOfElementLocated(profilePage.getSuccessAlertLocator()));
            assertTrue(success.isDisplayed(),
                caseMessage(meta, "Valid profile update must show success alert"));
        } else {
            boolean hasValidationOrError =
                !driver.findElements(profilePage.getValidationErrorLocator()).isEmpty()
                || !driver.findElements(profilePage.getErrorAlertLocator()).isEmpty();
            assertTrue(hasValidationOrError,
                caseMessage(meta, "Invalid profile update must show validation or error message"));
        }

        assertTrue(profilePage.isOnProfilePage(),
            caseMessage(meta, "Profile submission should stay on Profile page"));
    }
}