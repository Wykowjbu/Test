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

@DisplayName("Post Delete Tests using CSV file only")
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
        loginPage.login("phanhuy12", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: content={0}, expected={1}")
    @CsvFileSource(resources = "/post-delete-data.csv", numLinesToSkip = 1)
    @DisplayName("Should delete post based on CSV data")
    void testPostDeleteFromCsv(String content, String expected) {
        // Create a unique post to delete
        String marker = String.valueOf(System.currentTimeMillis());
        String postContent = content.trim() + " #" + marker;

        postCreatePage.navigate();
        postCreatePage.createPost(postContent);
        wait.until(ExpectedConditions.urlContains("/Posts"));

        // Navigate to My Posts and verify post exists
        myPostsPage.navigate();
        assertTrue(myPostsPage.containsPostContent(postContent),
                "Created post should be present before delete");

        // Click delete on the specific post
        myPostsPage.clickDeleteByContent(postContent);
        assertTrue(postDeletePage.isOnDeletePage(),
                "Should navigate to delete confirmation page");

        // Confirm deletion
        postDeletePage.confirmDelete();
        wait.until(ExpectedConditions.urlContains("/Posts"));

        // Reload My Posts page to verify deletion result
        myPostsPage.navigate();

        if ("success".equalsIgnoreCase(expected.trim())) {
            assertFalse(myPostsPage.containsPostContent(postContent),
                    "Deleted post should no longer appear in list");
        } else {
            assertTrue(myPostsPage.containsPostContent(postContent),
                    "Post should remain when delete is not successful");
        }
    }
}