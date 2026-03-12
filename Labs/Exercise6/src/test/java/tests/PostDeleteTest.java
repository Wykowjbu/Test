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
import pages.PostDeletePage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Post Delete Tests - Template Driven")
public class PostDeleteTest extends BaseTest {

    private static LoginPage loginPage;
    private static PostCreatePage postCreatePage;
    private static MyPostsPage myPostsPage;
    private static PostDeletePage postDeletePage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        postCreatePage = new PostCreatePage(driver);
        myPostsPage = new MyPostsPage(driver);
        postDeletePage = new PostDeletePage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginAndSetup() {
        loginPage.navigate();
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/post-delete-data.csv", numLinesToSkip = 1)
    @DisplayName("Delete post scenarios from standardized CSV")
    void testPostDeleteFromTemplateCsv(String testCaseId,
                                       String testCaseDescription,
                                       String preConditions,
                                       String testCaseProcedure,
                                       String testData,
                                       String expectedResults,
                                       String priority,
                                       String severity,
                                       String status,
                                       String content,
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
        String postContent = clean(content) + " #" + marker;
        String resolvedExpected = clean(automationExpected);

        postCreatePage.navigate();
        postCreatePage.createPost(postContent);
        wait.until(ExpectedConditions.urlContains("/Posts"));

        myPostsPage.navigate();
        assertTrue(myPostsPage.containsPostContent(postContent),
            caseMessage(meta, "Seed post must exist before delete workflow starts"));

        myPostsPage.clickDeleteByContent(postContent);
        assertTrue(postDeletePage.isOnDeletePage(),
            caseMessage(meta, "Must navigate to Delete confirmation page"));
        assertTrue(postDeletePage.isWarningDisplayed(),
            caseMessage(meta, "Warning alert must be visible on Delete confirmation page"));

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            postDeletePage.confirmDelete();
            wait.until(ExpectedConditions.urlContains("/Posts"));

            myPostsPage.navigate();
            assertFalse(myPostsPage.containsPostContent(postContent),
                caseMessage(meta, "Deleted post must not appear in posts list"));
            return;
        }

        if (expect(resolvedExpected, EXPECT_CANCEL)) {
            postDeletePage.clickCancel();
            wait.until(ExpectedConditions.urlContains("/Posts"));

            myPostsPage.navigate();
            assertTrue(myPostsPage.containsPostContent(postContent),
                caseMessage(meta, "Cancelled delete must keep post in posts list"));
            return;
        }

        fail(caseMessage(meta, "Unsupported automation_expected value: " + resolvedExpected));
    }
}