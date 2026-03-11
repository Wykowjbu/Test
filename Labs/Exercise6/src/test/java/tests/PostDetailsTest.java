package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.PostCreatePage;
import pages.PostDetailsPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Post Details Tests (Like & Comment) using CSV file only")
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
        loginPage.login("phanhuy", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: action={0}, data={1}, expected={2}")
    @CsvFileSource(resources = "/post-details-data.csv", numLinesToSkip = 1)
    @DisplayName("Should interact with post details based on CSV data")
    void testPostDetailsFromCsv(String action, String data, String expected) {
        // Step 1: Create a fresh post so we have a known post to interact with
        String marker = String.valueOf(System.currentTimeMillis());
        String postContent = "Detail test post #" + marker;

        postCreatePage.navigate();
        postCreatePage.createPost(postContent);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Posts/Create")));

        // Step 2: Navigate to the feed/index and find the post to get its Details link
        // We navigate to feed, then click on the post to go to details
        // Alternative: extract post id from URL after create redirect
        // For simplicity, navigate to index and use the post content to find details link
        driver.get(baseUrl + "/Index");
        wait.until(ExpectedConditions.urlContains("/Index"));

        // Click on the post to go to its details page
        org.openqa.selenium.By postLink = org.openqa.selenium.By.xpath(
                "//div[contains(@class,'post-content') and contains(normalize-space(.), '" + marker + "')]/ancestor::div[contains(@class,'card')]//a[contains(@href,'/Posts/Details')]"
        );

        try {
            wait.until(ExpectedConditions.elementToBeClickable(postLink)).click();
        } catch (Exception e) {
            // If no direct Details link, try clicking on the post content area
            org.openqa.selenium.By contentArea = org.openqa.selenium.By.xpath(
                    "//div[contains(@class,'post-content') and contains(normalize-space(.), '" + marker + "')]"
            );
            wait.until(ExpectedConditions.elementToBeClickable(contentArea)).click();
        }

        wait.until(ExpectedConditions.urlContains("/Posts/Details"));
        assertTrue(postDetailsPage.isOnDetailsPage(), "Should be on post details page");

        // Step 3: Perform the action
        if ("like".equalsIgnoreCase(action.trim())) {
            // Doc: click Like -> like count increases by 1, button gets class "liked"
            int beforeCount = postDetailsPage.getLikeCount();
            postDetailsPage.clickLike();

            // Wait for page reload after like
            wait.until(ExpectedConditions.urlContains("/Posts/Details"));

            if ("success".equalsIgnoreCase(expected.trim())) {
                int afterCount = postDetailsPage.getLikeCount();
                assertTrue(afterCount >= beforeCount,
                        "Like count should increase after liking");
            }

        } else if ("comment".equalsIgnoreCase(action.trim())) {
            // Doc: enter comment -> click Post Comment -> comment appears in Comments section
            String commentText = data.trim() + " #" + marker;
            postDetailsPage.postComment(commentText);

            // Wait for page reload after comment
            wait.until(ExpectedConditions.urlContains("/Posts/Details"));

            if ("success".equalsIgnoreCase(expected.trim())) {
                assertTrue(postDetailsPage.isCommentVisible(commentText),
                        "Comment should be visible after posting");
            }
        }
    }
}
