package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Post Edit Tests using CSV file only")
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
        loginPage.login("phanhuy12", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: seed={0}, edited={1}, expected={2}")
    @CsvFileSource(resources = "/post-edit-data.csv", numLinesToSkip = 1)
    @DisplayName("Should edit post based on CSV data")
    void testPostEditFromCsv(String seedContent, String editedContent, String expected) {
        // Step 1: Create a seed post with unique marker
        String marker = String.valueOf(System.currentTimeMillis());
        String createdPostContent = seedContent.trim() + " #" + marker;

        postCreatePage.navigate();
        postCreatePage.createPost(createdPostContent);
        // After create, redirects to feed/index
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Posts/Create")));

        // Step 2: Go to My Posts and click Edit on the created post
        myPostsPage.navigate();
        myPostsPage.clickEditByContent(createdPostContent);
        assertTrue(postEditPage.isOnEditPage(), "Should be on edit page");

        // Step 3: Edit the content
        String resolvedEditedContent = resolveEditedContent(editedContent, marker);
        postEditPage.editPostContent(resolvedEditedContent);

        if ("success".equalsIgnoreCase(expected.trim())) {
            // Doc: success -> redirect to /Posts/Index with updated content visible
            wait.until(ExpectedConditions.urlContains("/Posts"));
            myPostsPage.navigate();
            assertTrue(myPostsPage.containsPostContent(resolvedEditedContent),
                    "Edited content should be shown in post list");
        } else {
            // Error: validation error displayed, stays on edit page
            WebElement validation = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(postEditPage.getValidationErrorLocator()));
            assertTrue(validation.isDisplayed(),
                    "Validation error should be shown for invalid edit data");
            assertTrue(postEditPage.isOnEditPage(),
                    "User should remain on edit page on invalid edit");
        }
    }

    private String resolveEditedContent(String input, String marker) {
        if (input == null) return "";
        String normalized = input.trim();
        if ("EMPTY".equalsIgnoreCase(normalized)) return "";
        if ("TOO_LONG".equalsIgnoreCase(normalized)) return "x".repeat(5001);
        return normalized + " #edited-" + marker;
    }
}