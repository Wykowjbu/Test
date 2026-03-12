package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * PostDeletePage: Page Object cho trang Xóa bài viết (/Posts/Delete/{id}).
 * Locators từ test_document Phần 1.7.
 * Yêu cầu đăng nhập & là chủ bài viết.
 */
public class PostDeletePage extends BasePage {

    // ===== LOCATORS từ test_document Phần 1.7 =====

    // Nút xác nhận xóa — doc: css=button[type='submit'].btn-danger
    // Expected text: "Yes, Delete This Post"
    private final By confirmDeleteButton = By.cssSelector("button[type='submit'].btn-danger");

    // Link Cancel — doc: css=a[href*='/Posts/Index']
    private final By cancelLink          = By.cssSelector("a[href*='/Posts/Index']");

    // Alert cảnh báo trên trang Delete — doc: css=div.alert.alert-warning
    private final By warningAlert        = By.cssSelector("div.alert.alert-warning");

    public PostDeletePage(WebDriver driver) {
        super(driver);
    }

    // === ACTIONS ===

    /**
     * Nhấn nút xóa xác nhận ("Yes, Delete This Post").
     * Thành công: redirect sang /Posts/Index, bài viết biến khỏi feed.
     */
    public void confirmDelete() {
        click(confirmDeleteButton);
    }

    /**
     * Nhấn Cancel để hủy xóa.
     * Kết quả: redirect sang /Posts/Index, bài viết vẫn tồn tại.
     */
    public void clickCancel() {
        click(cancelLink);
    }

    // === LOCATOR GETTERS ===

    /** Alert vàng cảnh báo trước khi xóa */
    public By getWarningAlertLocator() {
        return warningAlert;
    }

    // === VERIFICATIONS ===

    /** Kiểm tra đang ở trang Delete */
    public boolean isOnDeletePage() {
        return driver.getCurrentUrl().contains("/Posts/Delete");
    }

    public boolean isWarningDisplayed() {
        return isElementVisible(warningAlert);
    }
}