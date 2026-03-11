package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ChangePasswordPage extends BasePage {

    private static final String CHANGE_PASSWORD_URL = "https://localhost:7009/Account/ChangePassword";

    // Locators from doc: Id="Input_CurrentPassword", Id="Input_NewPassword", Id="Input_ConfirmPassword"
    private final By currentPasswordInput = By.id("Input_CurrentPassword");
    private final By newPasswordInput = By.id("Input_NewPassword");
    private final By confirmPasswordInput = By.id("Input_ConfirmPassword");
    // Doc: CssSelector="button[type='submit'].btn-primary.btn-lg"
    private final By submitButton = By.cssSelector("button[type='submit'].btn-primary.btn-lg");

    // Feedback locators
    // Doc: success => <div class="alert alert-success">
    private final By successAlert = By.cssSelector(".alert-success");
    // Doc: wrong old password => error alert (red)
    private final By errorAlert = By.cssSelector(".alert-danger");
    // Doc: mismatch / validation => .text-danger
    private final By validationError = By.cssSelector(".text-danger");

    public ChangePasswordPage(WebDriver driver) {
        super(driver);
    }

    public void navigate() {
        navigateTo(CHANGE_PASSWORD_URL);
    }

    // Combined action
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        type(currentPasswordInput, currentPassword);
        type(newPasswordInput, newPassword);
        type(confirmPasswordInput, confirmPassword);
        click(submitButton);
    }

    // Locator getters
    public By getSuccessAlertLocator() {
        return successAlert;
    }

    public By getErrorAlertLocator() {
        return errorAlert;
    }

    public By getValidationErrorLocator() {
        return validationError;
    }

    // Verifications
    public boolean isSuccessAlertDisplayed() {
        return isElementVisible(successAlert);
    }

    public boolean isOnChangePasswordPage() {
        return driver.getCurrentUrl().contains("/Account/ChangePassword");
    }
}