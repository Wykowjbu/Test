package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage: Page Object cho trang Đăng nhập (/Account/Login).
 * Locators từ test_document Phần 1.2.
 */
public class LoginPage extends BasePage {

    private static final String LOGIN_URL = "https://localhost:7009/Account/Login";

    // ===== LOCATORS từ test_document Phần 1.2 =====

    // id="Input_Username" — ASP.NET Razor: asp-for="Input.Username"
    private final By usernameInput    = By.id("Input_Username");
    // id="Input_Password"
    private final By passwordInput    = By.id("Input_Password");
    // id="Input_RememberMe" — checkbox (optional)
    private final By rememberMeCheckbox = By.id("Input_RememberMe");
    // Nút đăng nhập — doc: css=button[type='submit'].btn-primary
    private final By loginButton      = By.cssSelector("button[type='submit'].btn-primary");
    // Link đến trang Register — doc: css=a[href*='/Account/Register']
    private final By registerLink     = By.cssSelector("a[href*='/Account/Register']");

    // Alert thành công (hiển thị khi có message từ register redirect) — doc: css=div.alert.alert-success
    private final By successAlert     = By.cssSelector("div.alert.alert-success");
    // Alert lỗi server-side (sai username/password, tài khoản bị khóa) — doc: css=div.alert.alert-danger
    private final By errorAlert       = By.cssSelector("div.alert.alert-danger");
    // Span lỗi validation của username — doc: span[data-valmsg-for='Input.Username']
    private final By usernameValidation = fieldValidationLocator("Input.Username");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang Login, xóa cookie để đảm bảo chưa đăng nhập */
    public void navigate() {
        driver.manage().deleteAllCookies();
        navigateTo(LOGIN_URL);
    }

    // === ACTIONS ===

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    /** Tick/bỏ tick checkbox Remember Me */
    public void clickRememberMe() {
        click(rememberMeCheckbox);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    /** Thao tác nhập username + password rồi nhấn Login */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    // === LOCATOR GETTERS ===

    /** Alert xanh — "Registration successful! Please login." sau redirect từ Register */
    public By getSuccessAlertLocator() {
        return successAlert;
    }

    /** Alert đỏ — "Invalid username or password." / "Your account has been deactivated..." */
    public By getErrorAlertLocator() {
        return errorAlert;
    }

    /** Span lỗi username — "Username is required" */
    public By getUsernameValidationLocator() {
        return usernameValidation;
    }

    // === VERIFICATIONS ===

    /** Đăng nhập thành công = đã rời khỏi trang Login, redirect về / (Home Feed) */
    public boolean isLoginSuccessful() {
        return !driver.getCurrentUrl().contains("/Account/Login");
    }

    /** Vẫn ở trang Login — dùng khi kiểm tra validation fail */
    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/Account/Login");
    }

    public boolean isErrorAlertDisplayed() {
        return isElementVisible(errorAlert);
    }

    public String getErrorAlertText() {
        return getText(errorAlert);
    }
}
