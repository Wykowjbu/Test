package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PostCreatePage extends BasePage {

    private static final String CREATE_POST_URL = "https://localhost:7009/Posts/Create";

    // Locators from doc: Id="Input_Content", Id="Input_ImageFile"
    private final By contentTextarea = By.id("Input_Content");
    private final By imageFileInput = By.id("Input_ImageFile");
    // Doc: CssSelector="button[type='submit'].btn-primary"
    private final By submitButton = By.cssSelector("button[type='submit'].btn-primary");
    // Doc: CssSelector="a[href='/Posts/Index'].btn-outline-secondary"
    private final By cancelButton = By.cssSelector("a[href*='/Posts/Index'].btn-outline-secondary");

    // Feedback locators
    private final By errorMessage = By.cssSelector(".alert-danger");
    // asp-validation-for="Input.Content" generates .text-danger
    private final By validationError = By.cssSelector(".text-danger");

    public PostCreatePage(WebDriver driver) {
        super(driver);
    }

    // Navigation
    public void navigate() {
        navigateTo(CREATE_POST_URL);
    }

    // Page Actions
    public void enterContent(String content) {
        WebElement textarea = waitForVisibility(contentTextarea);
        textarea.clear();
        textarea.sendKeys(content);
    }

    public void submit() {
        click(submitButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    // Combined action
    public void createPost(String content) {
        enterContent(content);
        submit();
    }

    // Locator getters for WebDriverWait in tests
    public By getErrorMessageLocator() {
        return errorMessage;
    }

    public By getValidationErrorLocator() {
        return validationError;
    }

    // Verifications
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }

    public boolean isValidationErrorDisplayed() {
        return isElementVisible(validationError);
    }

    public String getErrorMessageText() {
        return getText(errorMessage);
    }

    public boolean isOnCreatePage() {
        return driver.getCurrentUrl().contains("/Posts/Create");
    }

    public boolean isRedirectedToFeed() {
        // Doc: success redirects to /Index (home feed)
        String url = driver.getCurrentUrl();
        return url.endsWith("/Index") || url.endsWith("/") || !url.contains("/Posts/Create");
    }
}