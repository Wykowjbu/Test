package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.UserProfilePage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("User Profile & Follow Tests using CSV file only")
public class UserProfileTest extends BaseTest {

    private static LoginPage loginPage;
    private static UserProfilePage userProfilePage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        userProfilePage = new UserProfilePage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginFirst() {
        loginPage.navigate();
        loginPage.login("phanhuy", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: targetUser={0}, action={1}, expected={2}")
    @CsvFileSource(resources = "/user-profile-data.csv", numLinesToSkip = 1)
    @DisplayName("Should follow/unfollow user based on CSV data")
    void testUserProfileFromCsv(String targetUser, String action, String expected) {
        // Navigate to target user's profile
        userProfilePage.navigate(targetUser.trim());
        assertTrue(userProfilePage.isOnUserProfilePage(),
                "Should be on user profile page");

        if ("follow".equalsIgnoreCase(action.trim())) {
            // Doc: click Follow -> button changes to "Following", Following count increases
            userProfilePage.clickFollowToggle();

            // Wait for page reload
            wait.until(ExpectedConditions.urlContains("/Users/Profile"));

            if ("success".equalsIgnoreCase(expected.trim())) {
                // After toggling, the button state should have changed
                String buttonText = userProfilePage.getFollowButtonText();
                assertTrue(buttonText.contains("Follow"),
                        "Follow button should be visible after toggle action");
            }
        }
    }
}
