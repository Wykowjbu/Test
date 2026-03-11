package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UserProfilePage extends BasePage {

    private static final String USER_PROFILE_BASE_URL = "https://localhost:7009/Users/Profile/";

    // Doc: Follow/Following button inside <form asp-page-handler="ToggleFollow">
    // "Follow" (btn-primary) when not following, "Following" (btn-outline-secondary) when following
    private final By followButton = By.cssSelector("form[action*='ToggleFollow'] button[type='submit']");
    private final By followingButton = By.cssSelector("form[action*='ToggleFollow'] button.btn-outline-secondary");
    private final By notFollowingButton = By.cssSelector("form[action*='ToggleFollow'] button.btn-primary");

    // Profile info
    private final By usernameDisplay = By.cssSelector("h3, h4, .username");
    private final By followersCount = By.xpath("//*[contains(text(),'Follower')]");
    private final By followingCount = By.xpath("//*[contains(text(),'Following')]");

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    public void navigate(String username) {
        navigateTo(USER_PROFILE_BASE_URL + username);
    }

    // Actions
    public void clickFollowToggle() {
        click(followButton);
    }

    // Verifications
    public boolean isFollowing() {
        try {
            // If "Following" button (btn-outline-secondary) is visible, user is following
            return driver.findElement(followingButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNotFollowing() {
        try {
            // If "Follow" button (btn-primary, not outline) is visible, user is NOT following
            return driver.findElement(notFollowingButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFollowButtonText() {
        return getText(followButton);
    }

    public boolean isOnUserProfilePage() {
        return driver.getCurrentUrl().contains("/Users/Profile");
    }
}
