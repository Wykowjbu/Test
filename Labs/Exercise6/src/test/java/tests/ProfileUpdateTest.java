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

@DisplayName("Profile Update Tests using CSV file only")
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
        loginPage.login("phanhuy", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: username={0}, email={1}, expected={5}")
    @CsvFileSource(resources = "/profile-update-data.csv", numLinesToSkip = 1)
    @DisplayName("Should update profile based on CSV data")
    void testProfileUpdateFromCsv(String username, String email, String fullName, String bio, String dateOfBirth, String expected) {
        profilePage.navigate();

        // Resolve values: KEEP means use current, EMPTY means clear
        String resolvedUsername = resolveValue(username, profilePage.getUsernameValue());
        String resolvedEmail = resolveValue(email, profilePage.getEmailValue());
        String resolvedFullName = resolveValue(fullName, profilePage.getFullNameValue());
        String resolvedBio = resolveValue(bio, profilePage.getBioValue());
        String resolvedDateOfBirth = resolveValue(dateOfBirth, profilePage.getDateOfBirthValue());

        profilePage.updateProfile(resolvedUsername, resolvedEmail, resolvedFullName, resolvedDateOfBirth, resolvedBio);

        if ("success".equalsIgnoreCase(expected.trim())) {
            // Doc: reload page shows <div class="alert alert-success"> and Bio text changed
            WebElement success = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(profilePage.getSuccessAlertLocator()));
            assertTrue(success.isDisplayed(),
                    "Success alert should be displayed for valid profile update");
        } else {
            // Error: validation error shown
            WebElement validation = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(profilePage.getValidationErrorLocator()));
            assertTrue(validation.isDisplayed(),
                    "Validation error should be displayed for invalid profile data");
        }

        assertTrue(profilePage.isOnProfilePage(),
                "User should remain on profile page after submitting");
    }

    private String resolveValue(String incoming, String currentValue) {
        if (incoming == null) return "";
        String normalized = incoming.trim();
        if ("KEEP".equalsIgnoreCase(normalized)) return currentValue == null ? "" : currentValue;
        if ("EMPTY".equalsIgnoreCase(normalized)) return "";
        return normalized;
    }
}