package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * MyPostsPage: Page Object cho trang Danh sách bài viết (/Posts/Index).
 * Dùng để kiểm tra danh sách bài viết sau các thao tác Create/Edit/Delete.
 * Locators từ test_document Phần 1 (Quick Reference URL Mapping).
 */
public class MyPostsPage extends BasePage {

    private static final String POSTS_INDEX_URL = "https://localhost:7009/Posts/Index";

    // ===== LOCATORS =====

    // Alert thành công sau khi tạo/xóa bài — doc: css=div.alert.alert-success
    // Ví dụ: "Post created successfully!" sau redirect từ Create
    private final By successAlert      = By.cssSelector("div.alert.alert-success");
    // Link tạo bài viết mới — trỏ về /Posts/Create
    private final By createPostButton  = By.cssSelector("a[href*='/Posts/Create']");

    public MyPostsPage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến trang danh sách bài viết */
    public void navigate() {
        navigateTo(POSTS_INDEX_URL);
    }

    // === VERIFICATIONS ===

    public boolean isOnPostsIndexPage() {
        return driver.getCurrentUrl().contains("/Posts");
    }

    /** Kiểm tra alert thành công (ví dụ sau khi tạo bài) */
    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successAlert);
    }

    /**
     * Kiểm tra bài viết với nội dung cho trước có xuất hiện trong danh sách không.
     * Dùng XPath tìm element chứa text trong class post-content.
     */
    public boolean containsPostContent(String content) {
        By contentLocator = By.xpath(
            "//div[contains(@class,'post-content') and contains(normalize-space(.), \""
            + escapeXpath(content) + "\")]"
        );
        return !driver.findElements(contentLocator).isEmpty();
    }

    /**
     * Click nút Edit trên post card xác định bằng nội dung bài viết.
     * Dùng XPath tổ hợp: tìm card chứa content, sau đó tìm link Edit bên trong.
     */
    public void clickEditByContent(String content) {
        By editButton = By.xpath(
            "//div[contains(@class,'post-card')][.//div[contains(@class,'post-content')"
            + " and contains(normalize-space(.), \"" + escapeXpath(content) + "\")]]"
            + "//a[contains(@href,'/Posts/Edit')]"
        );
        click(editButton);
    }

    /**
     * Click nút Delete trên post card xác định bằng nội dung bài viết.
     * Dùng XPath tổ hợp: tìm card chứa content, sau đó tìm link Delete bên trong.
     */
    public void clickDeleteByContent(String content) {
        By deleteButton = By.xpath(
            "//div[contains(@class,'post-card')][.//div[contains(@class,'post-content')"
            + " and contains(normalize-space(.), \"" + escapeXpath(content) + "\")]]"
            + "//a[contains(@href,'/Posts/Delete')]"
        );
        click(deleteButton);
    }

    /**
     * Click link Details của bài viết đầu tiên trong danh sách.
     * Dùng để điều hướng đến /Posts/Details/{id} cho test Like/Comment.
     */
    public void clickFirstPostDetails() {
        By detailsLink = By.cssSelector("div.post-card a[href*='/Posts/Details']");
        click(detailsLink);
    }

    private String escapeXpath(String value) {
        return value.replace("\"", "'");
    }
}