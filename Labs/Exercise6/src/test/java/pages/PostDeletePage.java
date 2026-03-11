package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PostDeletePage extends BasePage {

    // Locators matching Delete.cshtml
    private final By confirmDeleteButton = By.xpath("//button[@type='submit' and contains(., 'Yes, Delete This Post')]");
    private final By cancelButton = By.xpath("//a[contains(@href, '/Posts/Index')]");
    private final By warningAlert = By.cssSelector(".alert-warning");
    private final By postContentPreview = By.cssSelector(".post-content");

    public PostDeletePage(WebDriver driver) {
        super(driver);
    }

    // Actions
    public void confirmDelete() {
        click(confirmDeleteButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    // Locator getters
    public By getWarningAlertLocator() {
        return warningAlert;
    }

    // Verifications
    public boolean isOnDeletePage() {
        return driver.getCurrentUrl().contains("/Posts/Delete");
    }

    public boolean isWarningDisplayed() {
        return isElementVisible(warningAlert);
    }

    public String getPostContentPreview() {
        return getText(postContentPreview);
    }
}