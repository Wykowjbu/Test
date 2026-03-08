package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // URL for the login page
    private static final String LOGIN_URL = "https://localhost:7009/Account/Login";

    // Locators for the login page elements (matching ASP.NET Razor Pages tag helpers: asp-for="Input.XXX" => id="Input_XXX")
    private By usernameInput = By.id("Input_Username");
    private By passwordInput = By.id("Input_Password");
    private By rememberMeCheckbox = By.id("Input_RememberMe");
    private By loginButton = By.xpath("//button[@type='submit' and contains(., 'Login')]");

    // Locators for feedback messages
    private By successMessage = By.cssSelector(".alert-success");
    private By errorMessage = By.cssSelector(".alert-danger");
    private By validationError = By.cssSelector(".text-danger");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Navigate to the login page (clears cookies to ensure fresh session)
    public void navigate() {
        driver.manage().deleteAllCookies();
        navigateTo(LOGIN_URL);
    }

    // Page Actions
    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    // Combined action method
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    // Locator getters for use with WebDriverWait in tests
    public By getSuccessLocator() {
        return successMessage;
    }

    public By getErrorLocator() {
        return errorMessage;
    }

    public By getValidationErrorLocator() {
        return validationError;
    }

    // Verifications
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }

    public String getErrorMessageText() {
        return getText(errorMessage);
    }

    public boolean isLoginSuccessful() {
        // After successful login, user is redirected away from login page
        return !driver.getCurrentUrl().contains("/Account/Login");
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/Account/Login");
    }
}
