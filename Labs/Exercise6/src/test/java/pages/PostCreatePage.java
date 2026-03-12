package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * PostCreatePage: Page Object cho trang Tạo bài viết (/Posts/Create).
 * Locators từ test_document Phần 1.5.
 * Yêu cầu đăng nhập trước — nếu chưa sẽ redirect /Account/Login.
 */
public class PostCreatePage extends BasePage {

    private static final String CREATE_POST_URL = "https://localhost:7009/Posts/Create";

    // ===== LOCATORS từ test_document Phần 1.5 =====

    // id="Input_Content" — textarea nội dung bài viết (required)
    private final By contentTextarea  = By.id("Input_Content");
    // id="Input_ImageFile" — input file upload ảnh (optional)
    private final By imageFileInput   = By.id("Input_ImageFile");
    // Nút Post — doc: css=button[type='submit'].btn-primary
    private final By submitButton     = By.cssSelector("button[type='submit'].btn-primary");
    // Link Cancel — doc: css=a[href*='/Posts/Index']
    private final By cancelLink       = By.cssSelector("a[href*='/Posts/Index']");

    // Alert lỗi server — doc: css=div.alert.alert-danger
    // Ví dụ: "Only image files (jpg, jpeg, png, gif) are allowed.", "Image file size cannot exceed 10MB."
    private final By errorAlert       = By.cssSelector("div.alert.alert-danger");
    // Span validation của Content — doc: css=span[data-valmsg-for='Input.Content']
    // Expected: "Content is required."
    private final By contentValidation = fieldValidationLocator("Input.Content");

    public PostCreatePage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang tạo bài viết */
    public void navigate() {
        navigateTo(CREATE_POST_URL);
    }

    // === ACTIONS ===

    /** Nhập nội dung vào textarea */
    public void enterContent(String content) {
        WebElement textarea = waitForVisibility(contentTextarea);
        textarea.clear();
        textarea.sendKeys(content);
    }

    /** Nhấn nút Post để submit bài viết */
    public void submit() {
        click(submitButton);
    }

    /** Click Cancel để quay về /Posts/Index */
    public void clickCancel() {
        click(cancelLink);
    }

    /**
     * Tạo bài viết: nhập content rồi submit.
     * Sau khi thành công redirect sang /Posts/Index?message=Post created successfully!
     */
    public void createPost(String content) {
        enterContent(content);
        submit();
    }

    // === LOCATOR GETTERS ===

    /** Alert đỏ — lỗi server (ảnh sai định dạng, ảnh quá lớn, content > 5000 ký tự) */
    public By getErrorAlertLocator() {
        return errorAlert;
    }

    /** Span validation Content — "Content is required." (client-side khi để trống) */
    public By getContentValidationLocator() {
        return contentValidation;
    }

    // === VERIFICATIONS ===

    public boolean isErrorAlertDisplayed() {
        return isElementVisible(errorAlert);
    }

    public boolean isContentValidationDisplayed() {
        return isElementVisible(contentValidation);
    }

    public String getErrorAlertText() {
        return getText(errorAlert);
    }

    public boolean isOnCreatePage() {
        return driver.getCurrentUrl().contains("/Posts/Create");
    }

    /**
     * Kiểm tra đã redirect ra khỏi trang Create Post sau khi tạo thành công.
     * Doc: Redirect sang /Posts/Index?message=Post created successfully!
     */
    public boolean isRedirectedToFeed() {
        String url = driver.getCurrentUrl();
        return url.contains("/Posts/Index") || !url.contains("/Posts/Create");
    }
}