package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyPostsPage extends BasePage {

    private static final String POSTS_INDEX_URL = "https://localhost:7009/Posts/Index";

    // Locators matching doc / Index.cshtml
    private final By successMessage = By.cssSelector(".alert-success");
    private final By createPostButton = By.cssSelector("a[href*='/Posts/Create'].btn-primary");
    private final By emptyState = By.cssSelector(".empty-state");

    public MyPostsPage(WebDriver driver) {
        super(driver);
    }

    public void navigate() {
        navigateTo(POSTS_INDEX_URL);
    }

    // Verifications
    public boolean isOnPostsIndexPage() {
        return driver.getCurrentUrl().contains("/Posts");
    }

    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successMessage);
    }

    public boolean isEmptyStateDisplayed() {
        return isElementVisible(emptyState);
    }

    // Find post by content text
    public boolean containsPostContent(String content) {
        By contentLocator = By.xpath("//div[contains(@class,'post-content') and contains(normalize-space(.), \"" + escapeXpath(content) + "\")]");
        return !driver.findElements(contentLocator).isEmpty();
    }

    // Click Edit button on a specific post card by its content
    public void clickEditByContent(String content) {
        By editButton = By.xpath("//div[contains(@class,'post-card')][.//div[contains(@class,'post-content') and contains(normalize-space(.), \"" + escapeXpath(content) + "\")]]//a[contains(@href,'/Posts/Edit')]");
        click(editButton);
    }

    // Click Delete button on a specific post card by its content
    public void clickDeleteByContent(String content) {
        By deleteButton = By.xpath("//div[contains(@class,'post-card')][.//div[contains(@class,'post-content') and contains(normalize-space(.), \"" + escapeXpath(content) + "\")]]//a[contains(@href,'/Posts/Delete')]");
        click(deleteButton);
    }

    // Click the first post's Details link (to go to /Posts/Details/{id})
    public void clickFirstPostDetails() {
        By detailsLink = By.cssSelector("div.post-card a[href*='/Posts/Details']");
        click(detailsLink);
    }

    private String escapeXpath(String value) {
        return value.replace("\"", "'");
    }
}