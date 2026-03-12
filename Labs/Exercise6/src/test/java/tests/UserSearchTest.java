package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.UserSearchPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("User Search Tests - Template Driven")
public class UserSearchTest extends BaseTest {

    private static LoginPage loginPage;
    private static UserSearchPage userSearchPage;
    private static WebDriverWait wait;

    @BeforeAll
    static void initPages() {
        loginPage = new LoginPage(driver);
        userSearchPage = new UserSearchPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeEach
    void loginFirst() {
        loginPage.navigate();
        loginPage.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "[{index}] {0} - {8}")
    @CsvFileSource(resources = "/user-search-data.csv", numLinesToSkip = 1)
    @DisplayName("User search scenarios from standardized CSV")
    void testUserSearchFromTemplateCsv(String testCaseId,
                                       String testCaseDescription,
                                       String preConditions,
                                       String testCaseProcedure,
                                       String testData,
                                       String expectedResults,
                                       String priority,
                                       String severity,
                                       String status,
                                       String query,
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

        userSearchPage.navigate();
        userSearchPage.search(clean(query));

        String expectedValue = clean(automationExpected);
        if (expect(expectedValue, EXPECT_FOUND)) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(userSearchPage.getResultCardsLocator()));
            assertTrue(userSearchPage.getResultCount() > 0,
                caseMessage(meta, "Found scenario must return at least one user card"));
            return;
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(userSearchPage.getEmptyResultLocator()));
        assertTrue(userSearchPage.isEmptyResultDisplayed(),
            caseMessage(meta, "Empty scenario must display 'No users found' message"));
    }
}