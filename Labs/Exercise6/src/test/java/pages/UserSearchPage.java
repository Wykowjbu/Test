package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * UserSearchPage: Page Object cho trang Tìm kiếm người dùng (/Users/Search).
 * Locators từ test_document Phần 1.9.
 * Không yêu cầu đăng nhập để tìm kiếm, nhưng nút Follow chỉ hiện khi đã login.
 */
public class UserSearchPage extends BasePage {

    private static final String SEARCH_URL = "https://localhost:7009/Users/Search";

    // ===== LOCATORS từ test_document Phần 1.9 =====

    // Input tìm kiếm — doc: name="q"
    private final By queryInput          = By.name("q");
    // Nút Search — doc: css=button[type='submit'].btn-primary
    private final By searchButton        = By.cssSelector("button[type='submit'].btn-primary");
    // Nút Follow/Unfollow trong kết quả — doc: css=form[action*='ToggleFollow'] button[type='submit']
    private final By followToggleButton  = By.cssSelector("form[action*='ToggleFollow'] button[type='submit']");
    // Badge số kết quả — doc: css=span.badge.bg-secondary (ví dụ: "3 found")
    private final By resultsBadge        = By.cssSelector("span.badge.bg-secondary");
    // Thẻ kết quả người dùng — mỗi user là 1 div.card
    private final By resultCards         = By.cssSelector("div.card");
    // Khi không tìm thấy — doc: div.card chứa text "No users found"
    private final By emptyResultText     = By.xpath(
        "//div[contains(@class,'card') and contains(normalize-space(.), 'No users found')]"
        + " | //h5[contains(normalize-space(.), 'No users found')]"
    );
    // Heading khi không có keyword — doc: text "Find People"
    private final By findPeopleHeading   = By.xpath("//*[contains(normalize-space(.), 'Find People')]");

    public UserSearchPage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang Search (GET /Users/Search, không có keyword) */
    public void navigate() {
        navigateTo(SEARCH_URL);
    }

    // === ACTIONS ===

    /**
     * Tìm kiếm người dùng: nhập keyword vào input rồi nhấn Search.
     * URL sau: /Users/Search?q={keyword}
     */
    public void search(String query) {
        type(queryInput, query);
        click(searchButton);
    }

    /** Click nút Follow/Unfollow trên user đầu tiên trong kết quả */
    public void clickFollowToggle() {
        click(followToggleButton);
    }

    // === VERIFICATIONS ===

    /** Số lượng user card trong kết quả (> 0 nghĩa là tìm thấy) */
    public int getResultCount() {
        List<?> elements = driver.findElements(resultCards);
        return elements.size();
    }

    /**
     * Kiểm tra hiển thị "No users found" khi không có kết quả.
     * Doc: "No users found" / "Try searching with different keywords."
     */
    public boolean isEmptyResultDisplayed() {
        return isElementVisible(emptyResultText);
    }

    // === LOCATOR GETTERS ===

    /** Locator cho card kết quả user */
    public By getResultCardsLocator() {
        return resultCards;
    }

    /** Locator cho message "No users found" */
    public By getEmptyResultLocator() {
        return emptyResultText;
    }

    /** Badge hiển thị số kết quả — ví dụ: "3 found" */
    public By getResultsBadgeLocator() {
        return resultsBadge;
    }
}