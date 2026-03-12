package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * PostDetailsPage: Page Object cho trang Chi tiết bài viết (/Posts/Details/{id}).
 * Locators từ test_document Phần 1.8.
 * Tính năng: Like (toggle), Comment, Delete Comment.
 */
public class PostDetailsPage extends BasePage {

    private static final String BASE_URL = "https://localhost:7009";

    // ===== LOCATORS từ test_document Phần 1.8 =====

    // Nút Like — doc: css=form[action*='Like'] button[type='submit']
    // (khi đã like thì button có class "liked" → biểu tượng bi-heart-fill)
    private final By likeButton          = By.cssSelector("form[action*='Like'] button[type='submit']");
    // Nút Like khi đã liked (có class "liked") — dùng để kiểm tra trạng thái
    private final By likedButton         = By.cssSelector("form[action*='Like'] button[type='submit'].liked");

    // Textarea bình luận — doc: name="commentContent"
    private final By commentInput        = By.name("commentContent");
    // Nút Post Comment — doc: css=form[action*='AddComment'] button[type='submit']
    private final By postCommentButton   = By.cssSelector("form[action*='AddComment'] button[type='submit']");
    // Nút Delete Comment — doc: css=form[action*='DeleteComment'] button.text-danger
    private final By deleteCommentButton = By.cssSelector("form[action*='DeleteComment'] button.text-danger");

    // Link Edit Post — doc: css=a[href*='/Posts/Edit']
    private final By editPostLink        = By.cssSelector("a[href*='/Posts/Edit']");
    // Link Delete Post — doc: css=a[href*='/Posts/Delete']
    private final By deletePostLink      = By.cssSelector("a[href*='/Posts/Delete']");
    // Link Back to Feed — doc: css=a[href='/']
    private final By backToFeedLink      = By.cssSelector("a[href='/']");

    // Alert lỗi (ví dụ: "Comment cannot exceed 1000 characters." / "You cannot like your own post.")
    private final By errorAlert          = By.cssSelector("div.alert.alert-danger");
    // Alert info — "Login to post a comment." (khi chưa đăng nhập xem Details)
    private final By infoAlert           = By.cssSelector("div.alert.alert-info");

    public PostDetailsPage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /** Điều hướng đến chi tiết bài viết theo ID */
    public void navigate(int postId) {
        navigateTo(BASE_URL + "/Posts/Details/" + postId);
    }

    // === ACTIONS ===

    /**
     * Click Like → trang reload → Like count tăng 1, icon đổi thành bi-heart-fill.
     * Click Like lần 2 (Unlike) → Like count giảm 1, icon trở về bi-heart.
     */
    public void clickLike() {
        click(likeButton);
    }

    /** Nhập nội dung bình luận vào textarea */
    public void enterComment(String comment) {
        type(commentInput, comment);
    }

    /**
     * Nhấn nút Post Comment.
     * Thành công: trang reload, comment mới xuất hiện cuối danh sách.
     */
    public void clickPostComment() {
        click(postCommentButton);
    }

    /** Nhập comment rồi submit */
    public void postComment(String comment) {
        enterComment(comment);
        clickPostComment();
    }

    /** Nhấn Delete trên comment của mình */
    public void clickDeleteComment() {
        click(deleteCommentButton);
    }

    // === VERIFICATIONS ===

    /**
     * Kiểm tra bài viết hiện đang ở trạng thái đã "liked".
     * Khi liked: button có class "liked" (icon bi-heart-fill).
     */
    public boolean isLiked() {
        try {
            return driver.findElement(likedButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy số lượt Like hiện tại từ text (ví dụ: "5 likes").
     * Dùng XPath tìm span chứa số lượt like.
     */
    public int getLikeCount() {
        try {
            By likeSpan = By.xpath("//span[contains(normalize-space(.), 'like')]");
            String text = getText(likeSpan);
            String num = text.replaceAll("[^0-9]", "");
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Kiểm tra comment với nội dung cho trước có hiển thị trên trang không.
     * Dùng XPath tìm element chứa text.
     */
    public boolean isCommentVisible(String commentText) {
        By commentLocator = By.xpath(
            "//*[contains(normalize-space(.), '" + commentText.replace("'", "\\'") + "')]"
        );
        return !driver.findElements(commentLocator).isEmpty();
    }

    public boolean isOnDetailsPage() {
        return driver.getCurrentUrl().contains("/Posts/Details");
    }

    /** Alert đỏ — "Comment cannot exceed 1000 characters." / "You cannot like your own post." */
    public By getErrorAlertLocator() {
        return errorAlert;
    }

    /** Alert info — "Login to post a comment." khi chưa đăng nhập */
    public By getInfoAlertLocator() {
        return infoAlert;
    }
}
