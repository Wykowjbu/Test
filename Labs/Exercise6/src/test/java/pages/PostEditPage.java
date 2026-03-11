package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PostEditPage extends BasePage {

    // Locators from doc: Id="Input_Content", Id="Input_ImageFile"
    private final By contentTextarea = By.id("Input_Content");
    private final By imageFileInput = By.id("Input_ImageFile");
    // Doc: CssSelector="button[type='submit'].btn-warning"
    private final By saveButton = By.cssSelector("button[type='submit'].btn-warning");

    // Feedback locators
    private final By validationError = By.cssSelector(".text-danger");
    private final By successMessage = By.cssSelector(".alert-success");

    public PostEditPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions
    public void enterContent(String content) {
        WebElement textarea = waitForVisibility(contentTextarea);
        textarea.clear();
        textarea.sendKeys(content);
    }

    public void clickSave() {
        click(saveButton);
    }

    // Combined action: clear old content, type new, and save
    public void editPostContent(String newContent) {
        enterContent(newContent);
        clickSave();
    }

    // Locator getters
    public By getValidationErrorLocator() {
        return validationError;
    }

    public By getSuccessMessageLocator() {
        return successMessage;
    }

    // Verifications
    public boolean isOnEditPage() {
        return driver.getCurrentUrl().contains("/Posts/Edit");
    }

    public boolean isValidationErrorDisplayed() {
        return isElementVisible(validationError);
    }
}