package huyde180519.exercise5.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Wait for visibility
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Wait for element to be clickable
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Click safely
    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    // Send keys safely
    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    // Get text safely
    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    // Navigate to a URL
    public void navigateTo(String url) {
        driver.get(url);
    }

    // Check if element is present
    protected boolean isElementVisible(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Scroll to element using JavaScript
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Click using JavaScript (useful when element is hidden behind overlays)
    protected void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Remove element by CSS selector using JavaScript
    protected void removeElementByCSS(String cssSelector) {
        ((JavascriptExecutor) driver).executeScript(
                "var element = document.querySelector('" + cssSelector + "'); if(element) element.remove();"
        );
    }
}
