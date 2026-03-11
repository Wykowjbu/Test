package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage extends BasePage {

    private static final String PROFILE_URL = "https://localhost:7009/Account/Profile";

    // Locators from doc: Id="Profile_Username", Id="Profile_Email", etc.
    private final By usernameInput = By.id("Profile_Username");
    private final By emailInput = By.id("Profile_Email");
    private final By fullNameInput = By.id("Profile_FullName");
    private final By dateOfBirthInput = By.id("Profile_DateOfBirth");
    private final By bioTextarea = By.id("Profile_Bio");
    private final By avatarFileInput = By.id("Profile_AvatarFile");
    // Doc: CssSelector="button[type='submit'].btn-primary"
    private final By saveButton = By.cssSelector("button[type='submit'].btn-primary");

    // Feedback locators
    // Doc: <div class="alert alert-success">
    private final By successAlert = By.cssSelector(".alert-success");
    private final By errorAlert = By.cssSelector(".alert-danger");
    private final By validationError = By.cssSelector(".text-danger");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public void navigate() {
        navigateTo(PROFILE_URL);
    }

    // Getters for current field values
    public String getUsernameValue() {
        return waitForVisibility(usernameInput).getAttribute("value");
    }

    public String getEmailValue() {
        return waitForVisibility(emailInput).getAttribute("value");
    }

    public String getFullNameValue() {
        return waitForVisibility(fullNameInput).getAttribute("value");
    }

    public String getDateOfBirthValue() {
        return waitForVisibility(dateOfBirthInput).getAttribute("value");
    }

    public String getBioValue() {
        return waitForVisibility(bioTextarea).getAttribute("value");
    }

    // Individual field actions
    public void enterBio(String bio) {
        type(bioTextarea, bio);
    }

    public void clickSave() {
        click(saveButton);
    }

    // Combined action: update all fields at once
    public void updateProfile(String username, String email, String fullName, String dateOfBirth, String bio) {
        type(usernameInput, username);
        type(emailInput, email);
        type(fullNameInput, fullName);
        type(dateOfBirthInput, dateOfBirth);
        type(bioTextarea, bio);
        // Scroll to save button to avoid ElementClickInterceptedException
        scrollToElement(saveButton);
        click(saveButton);
    }

    // Locator getters
    public By getSuccessAlertLocator() {
        return successAlert;
    }

    public By getErrorAlertLocator() {
        return errorAlert;
    }

    public By getValidationErrorLocator() {
        return validationError;
    }

    // Verifications
    public boolean isSuccessAlertDisplayed() {
        return isElementVisible(successAlert);
    }

    public boolean isOnProfilePage() {
        return driver.getCurrentUrl().contains("/Account/Profile");
    }
}