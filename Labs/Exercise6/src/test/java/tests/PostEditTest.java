package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.MyPostsPage;
import pages.PostCreatePage;
import pages.PostEditPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Post Edit Tests - Template Driven")
public class PostEditTest extends BaseTest {

    private static LoginPage loginPage;
    private static PostCreatePage postCreatePage;
    private static MyPostsPage myPostsPage;
    private static PostEditPage postEditPage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        postCreatePage = new PostCreatePage(driver);
        myPostsPage = new MyPostsPage(driver);
        postEditPage = new PostEditPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginFirst() {
        loginPage.navigate();
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/post-edit-data.csv", numLinesToSkip = 1)
    @DisplayName("Edit post scenarios from standardized CSV")
    void testPostEditFromTemplateCsv(String testCaseId,
                                     String testCaseDescription,
                                     String preConditions,
                                     String testCaseProcedure,
                                     String testData,
                                     String expectedResults,
                                     String priority,
                                     String severity,
                                     String status,
                                     String seedContent,
                                     String editedContent,
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

        String marker = String.valueOf(System.currentTimeMillis());
        String createdPostContent = clean(seedContent) + " #" + marker;

        postCreatePage.navigate();
        postCreatePage.createPost(createdPostContent);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Posts/Create")));

        myPostsPage.navigate();
        myPostsPage.clickEditByContent(createdPostContent);
        assertTrue(postEditPage.isOnEditPage(),
            caseMessage(meta, "Must navigate to Edit page before submitting edited content"));

        String resolvedEditedContent = resolveEditedContent(editedContent, marker);
        String resolvedExpected = clean(automationExpected);
        postEditPage.editPostContent(resolvedEditedContent);

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            wait.until(ExpectedConditions.urlContains("/Posts"));
            myPostsPage.navigate();
            assertTrue(myPostsPage.containsPostContent(resolvedEditedContent),
                caseMessage(meta, "Edited content must appear in posts list after successful save"));
            return;
        }

        boolean hasValidationOrError =
            !driver.findElements(postEditPage.getContentValidationLocator()).isEmpty()
            || !driver.findElements(postEditPage.getErrorAlertLocator()).isEmpty();

        assertTrue(hasValidationOrError,
            caseMessage(meta, "Invalid edited content must show validation or error"));
        assertTrue(postEditPage.isOnEditPage(),
            caseMessage(meta, "Failure scenario must remain on Edit page"));
    }

    private String resolveEditedContent(String value, String marker) {
        String normalized = resolveEmptyToken(value);
        if (TOKEN_TOO_LONG.equalsIgnoreCase(normalized)) {
            return "x".repeat(5001);
        }
        if (normalized.isEmpty()) {
            return "";
        }
        return normalized + " #edited-" + marker;
    }
}