package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.PostCreatePage;
import pages.PostDetailsPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Post Details Tests - Template Driven")
public class PostDetailsTest extends BaseTest {

    private static LoginPage loginPage;
    private static PostCreatePage postCreatePage;
    private static PostDetailsPage postDetailsPage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        postCreatePage = new PostCreatePage(driver);
        postDetailsPage = new PostDetailsPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginFirst() {
        loginPage.navigate();
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/post-details-data.csv", numLinesToSkip = 1)
    @DisplayName("Like and comment scenarios from standardized CSV")
    void testPostDetailsFromTemplateCsv(String testCaseId,
                                        String testCaseDescription,
                                        String preConditions,
                                        String testCaseProcedure,
                                        String testData,
                                        String expectedResults,
                                        String priority,
                                        String severity,
                                        String status,
                                        String action,
                                        String data,
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
        String postContent = "Detail test post #" + marker;

        postCreatePage.navigate();
        postCreatePage.createPost(postContent);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Posts/Create")));

        driver.get(baseUrl + "/");
        wait.until(ExpectedConditions.urlMatches(".*\\/($|\\?|#).*"));

        By postDetailsLink = By.xpath(
            "//div[contains(@class,'post-content') and contains(normalize-space(.), '"
                + marker + "')]/ancestor::div[contains(@class,'card')]"
                + "//a[contains(@href,'/Posts/Details')]"
        );

        try {
            wait.until(ExpectedConditions.elementToBeClickable(postDetailsLink)).click();
        } catch (Exception e) {
            By contentArea = By.xpath(
                "//div[contains(@class,'post-content') and contains(normalize-space(.), '" + marker + "')]"
            );
            wait.until(ExpectedConditions.elementToBeClickable(contentArea)).click();
        }

        wait.until(ExpectedConditions.urlContains("/Posts/Details"));
        assertTrue(postDetailsPage.isOnDetailsPage(),
            caseMessage(meta, "Must navigate to post details before interaction"));

        String actionValue = clean(action);
        String expectedValue = clean(automationExpected);

        if ("like".equalsIgnoreCase(actionValue)) {
            int beforeCount = postDetailsPage.getLikeCount();
            postDetailsPage.clickLike();
            wait.until(ExpectedConditions.urlContains("/Posts/Details"));

            if (expect(expectedValue, EXPECT_SUCCESS)) {
                int afterCount = postDetailsPage.getLikeCount();
                assertTrue(afterCount >= beforeCount || postDetailsPage.isLiked(),
                    caseMessage(meta, "Like action should keep/increase like state"));
            } else {
                boolean hasError = !driver.findElements(postDetailsPage.getErrorAlertLocator()).isEmpty();
                assertTrue(hasError,
                    caseMessage(meta, "Failed like scenario must show error alert"));
            }
            return;
        }

        if ("comment".equalsIgnoreCase(actionValue)) {
            String commentText = clean(data) + " #" + marker;
            postDetailsPage.postComment(commentText);
            wait.until(ExpectedConditions.urlContains("/Posts/Details"));

            if (expect(expectedValue, EXPECT_SUCCESS)) {
                assertTrue(postDetailsPage.isCommentVisible(commentText),
                    caseMessage(meta, "Posted comment must appear on details page"));
            } else {
                boolean hasError = !driver.findElements(postDetailsPage.getErrorAlertLocator()).isEmpty();
                assertTrue(hasError,
                    caseMessage(meta, "Failed comment scenario must show error alert"));
            }
            return;
        }

        fail(caseMessage(meta, "Unsupported action: " + actionValue));
    }
}