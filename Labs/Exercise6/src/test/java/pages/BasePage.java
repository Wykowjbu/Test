package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage: Lớp cha cho tất cả Page Objects.
 * Cung cấp các hàm tiện ích (click, type, wait) dùng chung
 * để tránh lặp code và tăng tính ổn định (không dùng Thread.sleep).
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Explicit wait 10 giây — áp dụng cho tất cả thao tác chờ element
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        waitForClickability(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    /**
     * Kiểm tra element có hiển thị không.
     * Trả về false thay vì throw exception nếu không tìm thấy.
     */
    protected boolean isElementVisible(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cuộn trang đến element — dùng trước khi click nếu element bị che khuất.
     */
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Lấy locator cho span validation của từng field cụ thể.
     * ASP.NET Razor Pages render: asp-validation-for="Input.FieldName"
     * → <span data-valmsg-for="Input.FieldName">
     * Dùng để kiểm tra thông báo lỗi client-side của từng field.
     */
    protected By fieldValidationLocator(String fieldName) {
        return By.cssSelector("span[data-valmsg-for='" + fieldName + "']");
    }
}
