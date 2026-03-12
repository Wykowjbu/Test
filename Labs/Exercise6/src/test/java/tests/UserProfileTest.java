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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("User Profile Follow Tests - Template Driven")
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
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/user-profile-data.csv", numLinesToSkip = 1)
    @DisplayName("Follow and unfollow scenarios from standardized CSV")
    void testUserProfileFromTemplateCsv(String testCaseId,
                                        String testCaseDescription,
                                        String preConditions,
                                        String testCaseProcedure,
                                        String testData,
                                        String expectedResults,
                                        String priority,
                                        String severity,
                                        String status,
                                        String targetUser,
                                        String action,
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

        String targetUsername = clean(targetUser);
        String actionValue = clean(action);
        String expectedValue = clean(automationExpected);

        userProfilePage.navigate(targetUsername);
        assertTrue(userProfilePage.isOnUserProfilePage(),
            caseMessage(meta, "Must navigate to target user profile page"));

        if (!"follow".equalsIgnoreCase(actionValue)) {
            fail(caseMessage(meta, "Unsupported action: " + actionValue));
            return;
        }

        if (expect(expectedValue, EXPECT_SUCCESS)) {
            assertTrue(userProfilePage.isFollowButtonPresent(),
                caseMessage(meta, "Follow button must be visible for follow action"));

            userProfilePage.clickFollowToggle();
            wait.until(ExpectedConditions.urlContains("/Users/Profile"));

            String buttonText = userProfilePage.getFollowButtonText().toLowerCase();
            assertTrue(buttonText.contains("follow"),
                caseMessage(meta, "Follow button label must be Follow/Following after toggle"));
            return;
        }

        assertFalse(userProfilePage.isFollowButtonPresent(),
            caseMessage(meta, "Self-follow scenario must not display follow button"));
    }
}