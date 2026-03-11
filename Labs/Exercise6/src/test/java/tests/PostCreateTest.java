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
import pages.PostCreatePage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Create Post Tests using CSV data only")
public class PostCreateTest extends BaseTest {

    private static LoginPage loginPage;
    private static PostCreatePage postCreatePage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        postCreatePage = new PostCreatePage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginAndNavigate() {
        // Login before each test to ensure authenticated session
        loginPage.navigate();
        loginPage.login("phanhuy12", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
        postCreatePage.navigate();
    }

    @ParameterizedTest(name = "CSV File: content={0}, expected={1}")
    @CsvFileSource(resources = "/post-create-data.csv", numLinesToSkip = 1)
    @DisplayName("Should create post based on CSV data")
    void testCreatePostFromCsv(String content, String expected) {
        String resolvedContent = resolveContent(content);

        if (resolvedContent.isEmpty()) {
            // Doc: press Post without content -> shows validation text from asp-validation-for
            postCreatePage.submit();
            WebElement validation = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(postCreatePage.getValidationErrorLocator()));
            assertTrue(validation.isDisplayed(),
                    "Validation error should appear when content is empty");
            assertTrue(postCreatePage.isOnCreatePage(),
                    "Should stay on create page when content is empty");
        } else {
            postCreatePage.createPost(resolvedContent);

            if ("success".equalsIgnoreCase(expected.trim())) {
                // Doc: success -> redirects to /Index (Feed), post appears with correct text
                wait.until(ExpectedConditions.urlContains("/Index"));
                assertTrue(postCreatePage.isRedirectedToFeed(),
                        "Should redirect to feed after successful post creation");
            } else {
                // Error case (e.g. too long): stays on create page with error
                assertTrue(postCreatePage.isOnCreatePage(),
                        "Should stay on create page for invalid input");
            }
        }
    }

    private String resolveContent(String content) {
        if (content == null) return "";
        String normalized = content.trim();
        if ("EMPTY".equalsIgnoreCase(normalized)) return "";
        if ("TOO_LONG".equalsIgnoreCase(normalized)) return "x".repeat(5001);
        return normalized;
    }
}