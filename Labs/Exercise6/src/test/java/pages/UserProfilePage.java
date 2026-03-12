package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * UserProfilePage: Page Object cho trang Hồ sơ người dùng (/Users/Profile?username={u}).
 * Locators từ test_document Phần 2.9 (Follow/Unfollow).
 * URL dạng query param: /Users/Profile?username=phanhuy
 */
public class UserProfilePage extends BasePage {

    private static final String USER_PROFILE_BASE_URL = "https://localhost:7009/Users/Profile";

    // ===== LOCATORS từ test_document Phần 2.9 =====

    // Nút Follow/Unfollow — doc: css=form[action*='ToggleFollow'] button[type='submit']
    // Khi chưa follow: class="btn btn-primary" → text "Follow"
    // Khi đã follow: class="btn btn-outline-secondary" → text "Following"
    private final By followButton        = By.cssSelector("form[action*='ToggleFollow'] button[type='submit']");
    // Nút "Following" (đang follow) — có class btn-outline-secondary
    private final By followingButton     = By.cssSelector("form[action*='ToggleFollow'] button.btn-outline-secondary");
    // Nút "Follow" (chưa follow) — có class btn-primary
    private final By notFollowingButton  = By.cssSelector("form[action*='ToggleFollow'] button.btn-primary");

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    // === NAVIGATION ===

    /**
     * Điều hướng đến profile của user theo username.
     * URL: /Users/Profile?username={username}
     * Lưu ý: dùng query param "?username=" chứ KHÔNG phải path /Profile/{username}
     */
    public void navigate(String username) {
        navigateTo(USER_PROFILE_BASE_URL + "?username=" + username);
    }

    // === ACTIONS ===

    /**
     * Click Follow hoặc Unfollow (toggle).
     * Sau khi click: trang reload.
     * - Đang "Follow" → sau click thành "Following"
     * - Đang "Following" → sau click thành "Follow"
     */
    public void clickFollowToggle() {
        click(followButton);
    }

    // === VERIFICATIONS ===

    /**
     * Đang follow user này không?
     * Nếu có → nút "Following" (btn-outline-secondary) hiển thị.
     */
    public boolean isFollowing() {
        try {
            return driver.findElement(followingButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Chưa follow user này?
     * Nếu chưa → nút "Follow" (btn-primary) hiển thị.
     */
    public boolean isNotFollowing() {
        try {
            return driver.findElement(notFollowingButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Lấy text hiện tại của nút Follow (ví dụ: "Follow" hoặc "Following") */
    public String getFollowButtonText() {
        return getText(followButton);
    }

    /** Kiểm tra có nút Follow/Unfollow không (khi xem profile chính mình thì không có) */
    public boolean isFollowButtonPresent() {
        return !driver.findElements(followButton).isEmpty();
    }

    public boolean isOnUserProfilePage() {
        return driver.getCurrentUrl().contains("/Users/Profile");
    }
}
