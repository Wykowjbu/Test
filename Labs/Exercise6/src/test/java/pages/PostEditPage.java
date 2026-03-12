package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * PostEditPage: Page Object cho trang Chỉnh sửa bài viết (/Posts/Edit/{id}).
 * Locators từ test_document Phần 1.6.
 * Yêu cầu đăng nhập & là chủ bài viết.
 */
public class PostEditPage extends BasePage {

    // ===== LOCATORS từ test_document Phần 1.6 =====

    // id="Input_Content" — textarea nội dung (required)
    private final By contentTextarea   = By.id("Input_Content");
    // id="Input_ImageFile" — file upload ảnh (optional)
    private final By imageFileInput    = By.id("Input_ImageFile");
    // Nút Save Changes — doc: css=button[type='submit'].btn-warning
    private final By saveButton        = By.cssSelector("button[type='submit'].btn-warning");
    // Link Cancel — doc: css=a[href*='/Posts/Index']
    private final By cancelLink        = By.cssSelector("a[href*='/Posts/Index']");

    // Alert lỗi — doc: css=div.alert.alert-danger
    // Ví dụ: "You are not authorized...", content validation server-side
    private final By errorAlert        = By.cssSelector("div.alert.alert-danger");
    // Span validation Content — "Post content is required." hoặc lỗi client-side
    private final By contentValidation = fieldValidationLocator("Input.Content");

    public PostEditPage(WebDriver driver) {
        super(driver);
    }

    // === ACTIONS ===

    /** Nhập nội dung mới vào Content textarea */
    public void enterContent(String content) {
        WebElement textarea = waitForVisibility(contentTextarea);
        textarea.clear();
        textarea.sendKeys(content);
    }

    /** Nhấn Save Changes */
    public void clickSave() {
        click(saveButton);
    }

    /**
     * Cập nhật nội dung bài viết: xóa nội dung cũ, nhập mới rồi lưu.
     * Thành công: redirect sang /Posts/Index, bài viết hiển thị "(edited)"
     */
    public void editPostContent(String newContent) {
        enterContent(newContent);
        clickSave();
    }

    // === LOCATOR GETTERS ===

    /** Span validation Content — "Post content is required." */
    public By getContentValidationLocator() {
        return contentValidation;
    }

    /** Alert đỏ — "You are not authorized..." khi không phải owner */
    public By getErrorAlertLocator() {
        return errorAlert;
    }

    // === VERIFICATIONS ===

    public boolean isOnEditPage() {
        return driver.getCurrentUrl().contains("/Posts/Edit");
    }

    public boolean isContentValidationDisplayed() {
        return isElementVisible(contentValidation);
    }
}