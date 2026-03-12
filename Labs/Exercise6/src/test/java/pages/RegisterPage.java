package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * RegisterPage: Page Object cho trang Đăng ký (/Account/Register).
 * Locators được lấy từ test_document Phần 1.1.
 * ASP.NET Razor: asp-for="Input.XXX" → id="Input_XXX"
 */
public class RegisterPage extends BasePage {

    private static final String REGISTER_URL = "https://localhost:7009/Account/Register";

    // ===== LOCATORS từ test_document Phần 1.1 =====

    // Các input fields — id theo quy tắc ASP.NET Razor: asp-for="Input.XXX" → id="Input_XXX"
    private final By usernameInput       = By.id("Input_Username");
    private final By emailInput          = By.id("Input_Email");
    private final By fullNameInput       = By.id("Input_FullName");
    private final By passwordInput       = By.id("Input_Password");
    private final By confirmPasswordInput= By.id("Input_ConfirmPassword");

    // Nút Sign Up — doc: css=button[type='submit'].btn-success
    private final By registerButton      = By.cssSelector("button[type='submit'].btn-success");

    // Alert lỗi server-side (email/username đã tồn tại) — doc: css=div.alert.alert-danger
    private final By errorAlert          = By.cssSelector("div.alert.alert-danger");

    // Alert thành công (sau khi redirect về login) — doc: css=div.alert.alert-success
    private final By successAlert        = By.cssSelector("div.alert.alert-success");

    // Span validation lỗi của từng field cụ thể (client-side, dùng fieldValidationLocator())
    // doc: css=span[data-valmsg-for='Input.Username'], v.v.
    private final By usernameValidation       = fieldValidationLocator("Input.Username");
    private final By emailValidation          = fieldValidationLocator("Input.Email");
    private final By passwordValidation       = fieldValidationLocator("Input.Password");
    private final By confirmPasswordValidation= fieldValidationLocator("Input.ConfirmPassword");
    private final By fullNameValidation       = fieldValidationLocator("Input.FullName");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang Register, xóa cookie để đảm bảo phiên mới */
    public void navigate() {
        driver.manage().deleteAllCookies();
        navigateTo(REGISTER_URL);
    }

    // === ACTIONS ===

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void enterFullName(String fullName) {
        type(fullNameInput, fullName);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordInput, confirmPassword);
    }

    public void clickRegisterButton() {
        click(registerButton);
    }

    /**
     * Điền toàn bộ form đăng ký.
     * Nếu fullName rỗng/null thì bỏ qua (field optional theo doc).
     */
    public void fillRegistrationForm(String username, String email, String fullName,
                                     String password, String confirmPassword) {
        enterUsername(username);
        enterEmail(email);
        if (fullName != null && !fullName.isEmpty()) {
            enterFullName(fullName);
        }
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
    }

    public void submitRegistration() {
        clickRegisterButton();
    }

    // === LOCATOR GETTERS (để dùng với WebDriverWait ở test class) ===

    /** Alert đỏ dùng cho lỗi server-side (email/username trùng) */
    public By getErrorAlertLocator() {
        return errorAlert;
    }

    /** Alert xanh dùng cho thành công (hiển thị ở trang Login sau redirect) */
    public By getSuccessAlertLocator() {
        return successAlert;
    }

    /** Span validation của field Username — "Username is required", "Username already exists", v.v. */
    public By getUsernameValidationLocator() {
        return usernameValidation;
    }

    /** Span validation của field Email — "Invalid email format", v.v. */
    public By getEmailValidationLocator() {
        return emailValidation;
    }

    /** Span validation của field Password — "Password must be at least 6 characters", v.v. */
    public By getPasswordValidationLocator() {
        return passwordValidation;
    }

    /** Span validation của field Confirm Password — "Passwords do not match", v.v. */
    public By getConfirmPasswordValidationLocator() {
        return confirmPasswordValidation;
    }

    /** Span validation của field Full Name — "Full name cannot exceed 100 characters" */
    public By getFullNameValidationLocator() {
        return fullNameValidation;
    }

    // === VERIFICATIONS ===

    /** Kiểm tra đã redirect về trang Login sau đăng ký thành công */
    public boolean isRedirectedToLogin() {
        return driver.getCurrentUrl().contains("/Account/Login");
    }

    /** Kiểm tra vẫn đang ở trang Register (dùng khi validation fail) */
    public boolean isOnRegisterPage() {
        return driver.getCurrentUrl().contains("/Account/Register");
    }

    public boolean isErrorAlertDisplayed() {
        return isElementVisible(errorAlert);
    }

    public String getErrorAlertText() {
        return getText(errorAlert);
    }
}
