package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PostDetailsPage extends BasePage {

    // Locators from doc for /Posts/Details/{id}
    // Like button: CssSelector="button.post-action-btn" inside <form asp-page-handler="Like">
    private final By likeButton = By.cssSelector("form[action*='Like'] button.post-action-btn");
    // When already liked, the button also has class "liked"
    private final By likedButton = By.cssSelector("form[action*='Like'] button.post-action-btn.liked");

    // Comment box: Name="commentContent"
    private final By commentInput = By.name("commentContent");
    // Post Comment button: CssSelector="button[type='submit'].btn-primary.btn-sm"
    private final By postCommentButton = By.cssSelector("button[type='submit'].btn-primary.btn-sm");

    // Post content on details page
    private final By postContent = By.cssSelector(".post-content");

    // Like count text (contains "likes")
    private final By likeCountText = By.xpath("//*[contains(@class,'text-danger')]/ancestor::span | //*[contains(text(),'like')]");

    // Comments section: each comment block
    private final By commentTexts = By.cssSelector(".comment-content, .card-body p");

    // Delete comment button (visible only for own comments)
    private final By deleteCommentButton = By.cssSelector("button.text-danger");

    public PostDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void navigate(int postId) {
        navigateTo("https://localhost:7009/Posts/Details/" + postId);
    }

    // Actions
    public void clickLike() {
        click(likeButton);
    }

    public void enterComment(String comment) {
        type(commentInput, comment);
    }

    public void clickPostComment() {
        click(postCommentButton);
    }

    // Combined action
    public void postComment(String comment) {
        enterComment(comment);
        clickPostComment();
    }

    // Verifications
    public boolean isLiked() {
        try {
            return driver.findElement(likedButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLikeCountText() {
        By likeSpan = By.xpath("//span[contains(.,'like')]");
        return getText(likeSpan);
    }

    public int getLikeCount() {
        String text = getLikeCountText();
        // Extract number from text like "5 likes"
        String num = text.replaceAll("[^0-9]", "");
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }

    public boolean isCommentVisible(String commentText) {
        By commentLocator = By.xpath("//*[contains(normalize-space(.), '" + commentText.replace("'", "\\'") + "')]");
        return !driver.findElements(commentLocator).isEmpty();
    }

    public String getPostContentText() {
        return getText(postContent);
    }

    public boolean isOnDetailsPage() {
        return driver.getCurrentUrl().contains("/Posts/Details");
    }
}
