package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * ChangePasswordPage: Page Object cho trang Đổi mật khẩu (/Account/ChangePassword).
 * Locators từ test_document Phần 1.3.
 * Yêu cầu đăng nhập trước khi truy cập — nếu chưa đăng nhập sẽ redirect /Account/Login.
 */
public class ChangePasswordPage extends BasePage {

    private static final String CHANGE_PASSWORD_URL = "https://localhost:7009/Account/ChangePassword";

    // ===== LOCATORS từ test_document Phần 1.3 =====

    // id="Input_CurrentPassword"
    private final By currentPasswordInput = By.id("Input_CurrentPassword");
    // id="Input_NewPassword"
    private final By newPasswordInput     = By.id("Input_NewPassword");
    // id="Input_ConfirmPassword" — Confirm New Password
    private final By confirmPasswordInput = By.id("Input_ConfirmPassword");
    // Nút Change Password — doc: css=button[type='submit'].btn-primary
    // Không có class .btn-lg trong tài liệu => dùng .btn-primary
    private final By submitButton         = By.cssSelector("button[type='submit'].btn-primary");
    // Link quay lại Profile — doc: css=a[href*='/Account/Profile']
    private final By backToProfileLink    = By.cssSelector("a[href*='/Account/Profile']");

    // Alert thành công — doc: css=div.alert.alert-success
    // Expected: "Password changed successfully."
    private final By successAlert         = By.cssSelector("div.alert.alert-success");
    // Alert lỗi server (sai current password) — doc: css=div.alert.alert-danger
    // Expected: "Current password is incorrect."
    private final By errorAlert           = By.cssSelector("div.alert.alert-danger");

    // Span validation từng field (client-side: confirm mismatch, password yếu, v.v.)
    private final By currentPwValidation  = fieldValidationLocator("Input.CurrentPassword");
    private final By newPwValidation      = fieldValidationLocator("Input.NewPassword");
    private final By confirmPwValidation  = fieldValidationLocator("Input.ConfirmPassword");

    public ChangePasswordPage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang Change Password (phải đã đăng nhập) */
    public void navigate() {
        navigateTo(CHANGE_PASSWORD_URL);
    }

    // === ACTIONS ===

    /**
     * Thực hiện đổi mật khẩu:
     * 1. Nhập mật khẩu hiện tại
     * 2. Nhập mật khẩu mới
     * 3. Xác nhận mật khẩu mới
     * 4. Click nút Change Password
     */
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        type(currentPasswordInput, currentPassword);
        type(newPasswordInput, newPassword);
        type(confirmPasswordInput, confirmPassword);
        click(submitButton);
    }

    // === LOCATOR GETTERS ===

    /** Alert xanh — "Password changed successfully." */
    public By getSuccessAlertLocator() {
        return successAlert;
    }

    /** Alert đỏ — "Current password is incorrect." */
    public By getErrorAlertLocator() {
        return errorAlert;
    }

    /** Span lỗi confirm password — "Passwords do not match" (client-side) */
    public By getConfirmPasswordValidationLocator() {
        return confirmPwValidation;
    }

    /** Span lỗi new password — "Password must be at least 6 characters" (client-side) */
    public By getNewPasswordValidationLocator() {
        return newPwValidation;
    }

    // === VERIFICATIONS ===

    public boolean isSuccessAlertDisplayed() {
        return isElementVisible(successAlert);
    }

    /** Vẫn đang ở trang ChangePassword (dùng khi error xảy ra) */
    public boolean isOnChangePasswordPage() {
        return driver.getCurrentUrl().contains("/Account/ChangePassword");
    }
}