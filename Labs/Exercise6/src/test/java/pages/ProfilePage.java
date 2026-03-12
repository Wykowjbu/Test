package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * ProfilePage: Page Object cho trang Chỉnh sửa Profile (/Account/Profile).
 * Locators từ test_document Phần 1.4.
 * Yêu cầu đăng nhập — nếu chưa sẽ redirect /Account/Login.
 */
public class ProfilePage extends BasePage {

    private static final String PROFILE_URL = "https://localhost:7009/Account/Profile";

    // ===== LOCATORS từ test_document Phần 1.4 =====
    // Profile page dùng prefix "Profile_" khác với Register/Login dùng "Input_"

    // id="Profile_Username"
    private final By usernameInput      = By.id("Profile_Username");
    // id="Profile_Email"
    private final By emailInput         = By.id("Profile_Email");
    // id="Profile_FullName" (optional)
    private final By fullNameInput      = By.id("Profile_FullName");
    // id="Profile_DateOfBirth" (optional)
    private final By dateOfBirthInput   = By.id("Profile_DateOfBirth");
    // id="Profile_Bio" (optional, textarea)
    private final By bioTextarea        = By.id("Profile_Bio");
    // id="Profile_AvatarFile" (optional, file upload)
    private final By avatarFileInput    = By.id("Profile_AvatarFile");
    // Nút Save Changes — doc: css=button[type='submit'].btn-primary
    private final By saveButton         = By.cssSelector("button[type='submit'].btn-primary");
    // Link Change Password — doc: css=a[href*='/Account/ChangePassword']
    private final By changePasswordLink = By.cssSelector("a[href*='/Account/ChangePassword']");

    // Alert thành công — doc: css=div.alert.alert-success
    private final By successAlert       = By.cssSelector("div.alert.alert-success");
    // Alert lỗi — doc: css=div.alert.alert-danger
    private final By errorAlert         = By.cssSelector("div.alert.alert-danger");
    // Span validation bất kỳ field nào (dùng chung)
    private final By anyValidationError = By.cssSelector("span.text-danger");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang Profile (phải đã đăng nhập) */
    public void navigate() {
        navigateTo(PROFILE_URL);
    }

    // === GETTERS (đọc giá trị hiện tại của các field) ===

    /** Lấy username hiện tại — dùng để kiểm tra hoặc giữ nguyên khi update */
    public String getUsernameValue() {
        return waitForVisibility(usernameInput).getAttribute("value");
    }

    public String getEmailValue() {
        return waitForVisibility(emailInput).getAttribute("value");
    }

    public String getFullNameValue() {
        return waitForVisibility(fullNameInput).getAttribute("value");
    }

    public String getDateOfBirthValue() {
        return waitForVisibility(dateOfBirthInput).getAttribute("value");
    }

    public String getBioValue() {
        return waitForVisibility(bioTextarea).getAttribute("value");
    }

    // === ACTIONS ===

    public void enterBio(String bio) {
        type(bioTextarea, bio);
    }

    /** Cuộn đến nút Save rồi click (tránh ElementClickInterceptedException) */
    public void clickSave() {
        scrollToElement(saveButton);
        click(saveButton);
    }

    /**
     * Cập nhật tất cả các field rồi lưu.
     * Lưu ý: username là required, bio và dateOfBirth là optional nhưng vẫn nhập.
     */
    public void updateProfile(String username, String email, String fullName,
                              String dateOfBirth, String bio) {
        type(usernameInput, username);
        type(emailInput, email);
        type(fullNameInput, fullName);
        type(dateOfBirthInput, dateOfBirth);
        type(bioTextarea, bio);
        // Cuộn đến nút Save để đảm bảo nút không bị che
        scrollToElement(saveButton);
        click(saveButton);
    }

    // === LOCATOR GETTERS ===

    public By getSuccessAlertLocator() {
        return successAlert;
    }

    public By getErrorAlertLocator() {
        return errorAlert;
    }

    /** Span lỗi bất kỳ field validation trên trang Profile */
    public By getValidationErrorLocator() {
        return anyValidationError;
    }

    // === VERIFICATIONS ===

    public boolean isSuccessAlertDisplayed() {
        return isElementVisible(successAlert);
    }

    public boolean isOnProfilePage() {
        return driver.getCurrentUrl().contains("/Account/Profile");
    }
}