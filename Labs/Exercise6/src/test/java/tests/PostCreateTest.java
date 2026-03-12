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

@DisplayName("Create Post Tests - Template Driven")
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
        loginPage.navigate();
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
        postCreatePage.navigate();
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/post-create-data.csv", numLinesToSkip = 1)
    @DisplayName("Create post scenarios from standardized CSV")
    void testCreatePostFromTemplateCsv(String testCaseId,
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

        String resolvedContent = resolveContent(content);
        String resolvedExpected = clean(automationExpected);

        if (resolvedContent.isEmpty()) {
            postCreatePage.submit();
            WebElement validation = wait.until(
                ExpectedConditions.visibilityOfElementLocated(postCreatePage.getContentValidationLocator()));
            assertTrue(validation.isDisplayed(),
                caseMessage(meta, "Empty content must trigger content validation"));
            assertTrue(postCreatePage.isOnCreatePage(),
                caseMessage(meta, "Empty content must keep user on Create page"));
            return;
        }

        postCreatePage.createPost(resolvedContent);

        if (expect(resolvedExpected, EXPECT_SUCCESS)) {
            wait.until(ExpectedConditions.urlContains("/Posts?message=Post%20created%20successfully!"));
            assertTrue(postCreatePage.isRedirectedToFeed(),
                caseMessage(meta, "Valid post content must redirect to posts index"));
            return;
        }

        boolean onCreateOrHasError =
            postCreatePage.isOnCreatePage()
            || !driver.findElements(postCreatePage.getErrorAlertLocator()).isEmpty();

        assertTrue(onCreateOrHasError,
            caseMessage(meta, "Invalid post content must remain on Create page or show error alert"));
    }

    private String resolveContent(String value) {
        String normalized = resolveEmptyToken(value);
        if (TOKEN_TOO_LONG.equalsIgnoreCase(normalized)) {
            return "x".repeat(5001);
        }
        return normalized;
    }
}