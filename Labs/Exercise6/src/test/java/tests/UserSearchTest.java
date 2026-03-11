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

@DisplayName("User Search Tests using CSV file only")
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
        loginPage.login("phanhuy", "1234567");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
    }

    @ParameterizedTest(name = "CSV: query={0}, expected={1}")
    @CsvFileSource(resources = "/user-search-data.csv", numLinesToSkip = 1)
    @DisplayName("Should search users based on CSV data")
    void testUserSearchFromCsv(String query, String expected) {
        userSearchPage.navigate();
        userSearchPage.search(query);

        if ("found".equalsIgnoreCase(expected.trim())) {
            // Doc: result shows div.card with user info and Follow button
            wait.until(ExpectedConditions.visibilityOfElementLocated(userSearchPage.getResultCardsLocator()));
            assertTrue(userSearchPage.getResultCount() > 0,
                    "Search result should contain at least one user");
        } else {
            // Doc: <h5>No users found</h5>
            assertTrue(userSearchPage.isEmptyResultDisplayed(),
                    "Empty search message 'No users found' should be displayed");
        }
    }
}