package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class UserSearchPage extends BasePage {

    private static final String SEARCH_URL = "https://localhost:7009/Users/Search";

    // Locators from doc: Name="q"
    private final By queryInput = By.name("q");
    // Doc: CssSelector="button[type='submit'].btn-primary"
    private final By searchButton = By.cssSelector("button[type='submit'].btn-primary");
    // Doc: result cards are div.card
    private final By resultCards = By.cssSelector("div.card");
    // Doc: "No users found" => <h5>No users found</h5>
    private final By emptyResultText = By.xpath("//h5[contains(text(), 'No users found')]");

    public UserSearchPage(WebDriver driver) {
        super(driver);
    }

    public void navigate() {
        navigateTo(SEARCH_URL);
    }

    // Combined action
    public void search(String query) {
        type(queryInput, query);
        click(searchButton);
    }

    // Verifications
    public int getResultCount() {
        List<?> elements = driver.findElements(resultCards);
        return elements.size();
    }

    public boolean isEmptyResultDisplayed() {
        return isElementVisible(emptyResultText);
    }

    // Locator getters
    public By getResultCardsLocator() {
        return resultCards;
    }

    public By getEmptyResultLocator() {
        return emptyResultText;
    }
}