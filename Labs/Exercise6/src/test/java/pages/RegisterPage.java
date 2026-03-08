package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    // URL for the register page
    private static final String REGISTER_URL = "https://localhost:7009/Account/Register";

    // Locators for Register form (matching ASP.NET Razor Pages tag helpers: asp-for="Input.XXX" => id="Input_XXX")
    private By usernameInput = By.id("Input_Username");
    private By emailInput = By.id("Input_Email");
    private By fullNameInput = By.id("Input_FullName");
    private By passwordInput = By.id("Input_Password");
    private By confirmPasswordInput = By.id("Input_ConfirmPassword");
    private By registerButton = By.xpath("//button[@type='submit' and contains(., 'Sign Up')]");

    // Locators for feedback messages
    private By errorMessage = By.cssSelector(".alert-danger");
    private By validationError = By.cssSelector(".text-danger");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // Navigate to the register page (clears cookies to ensure fresh session)
    public void navigate() {
        driver.manage().deleteAllCookies();
        navigateTo(REGISTER_URL);
    }

    // Page Actions
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

    // Combined action methods
    public void fillRegistrationForm(String username, String email, String fullName, String password, String confirmPassword) {
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

    // Locator getters for use with WebDriverWait in tests
    public By getErrorLocator() {
        return errorMessage;
    }

    public By getValidationErrorLocator() {
        return validationError;
    }

    // Verifications
    public boolean isValidationErrorPresent() {
        return isElementVisible(validationError);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }

    public String getErrorMessageText() {
        return getText(errorMessage);
    }

    public boolean isRedirectedToLogin() {
        return driver.getCurrentUrl().contains("/Account/Login");
    }

    public boolean isOnRegisterPage() {
        return driver.getCurrentUrl().contains("/Account/Register");
    }
}
