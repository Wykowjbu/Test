package huyde180519.exercise5.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PracticeFormPage extends BasePage {

    // URL
    private static final String URL = "https://demoqa.com/automation-practice-form";

    // Locators
    private By firstNameField = By.id("firstName");
    private By lastNameField = By.id("lastName");
    private By emailField = By.id("userEmail");
    private By mobileField = By.id("userNumber");
    private By subjectsField = By.id("subjectsInput");
    private By currentAddressField = By.id("currentAddress");
    private By submitButton = By.id("submit");

    // Modal
    private By modalTitle = By.id("example-modal-sizes-title-lg");
    private By modalBody = By.cssSelector(".modal-body");
    private By modalCloseButton = By.id("closeLargeModal");

    // Date of Birth
    private By dateOfBirthInput = By.id("dateOfBirthInput");

    // State and City dropdowns
    private By stateDropdown = By.id("state");
    private By stateInput = By.id("react-select-3-input");
    private By cityDropdown = By.id("city");
    private By cityInput = By.id("react-select-4-input");

    // Constructor
    public PracticeFormPage(WebDriver driver) {
        super(driver);
    }

    // Actions
    public void navigate() {
        navigateTo(URL);
        // Chờ form React render xong trước khi thao tác
        waitForVisibility(firstNameField);
        // Remove the fixed footer ad and Google ad banners that block clicks
        try { removeElementByCSS("#fixedban"); } catch (Exception ignored) {}
        try { removeElementByCSS("footer"); } catch (Exception ignored) {}
        try { removeElementByCSS("#adplus-anchor"); } catch (Exception ignored) {}
        try { removeElementByCSS(".google-auto-placed"); } catch (Exception ignored) {}
        try { removeElementByCSS("#Ad\\.Plus-728x90"); } catch (Exception ignored) {}
        try { removeElementByCSS("iframe[id^='google_ads']"); } catch (Exception ignored) {}
    }

    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }

    public void enterEmail(String email) {
        type(emailField, email);
    }

    public void selectGender(String gender) {
        // Gender radio buttons: Male, Female, Other
        By genderRadio = By.xpath("//label[text()='" + gender + "']");
        scrollToElement(genderRadio);
        jsClick(genderRadio);
    }

    public void enterMobile(String mobile) {
        type(mobileField, mobile);
    }

    public void enterDateOfBirth(String day, String month, String year) {
        // Click on date input to open the date picker
        driver.findElement(dateOfBirthInput).click();

        // Select year
        By yearSelect = By.cssSelector(".react-datepicker__year-select");
        new org.openqa.selenium.support.ui.Select(driver.findElement(yearSelect)).selectByValue(year);

        // Select month (0-indexed)
        By monthSelect = By.cssSelector(".react-datepicker__month-select");
        new org.openqa.selenium.support.ui.Select(driver.findElement(monthSelect)).selectByVisibleText(month);

        // Select day
        By dayOption = By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'outside-month')) and text()='" + day + "']");
        click(dayOption);
    }

    public void enterSubjects(String subject) {
        WebElement subjectInput = driver.findElement(subjectsField);
        subjectInput.sendKeys(subject);
        subjectInput.sendKeys(Keys.ENTER);
    }

    public void selectHobby(String hobby) {
        // Hobbies: Sports, Reading, Music
        By hobbyCheckbox = By.xpath("//label[text()='" + hobby + "']");
        scrollToElement(hobbyCheckbox);
        jsClick(hobbyCheckbox);
    }

    public void enterCurrentAddress(String address) {
        type(currentAddressField, address);
    }

    public void selectState(String state) {
        scrollToElement(stateDropdown);
        jsClick(stateDropdown);
        WebElement input = driver.findElement(stateInput);
        input.sendKeys(state);
        input.sendKeys(Keys.ENTER);
    }

    public void selectCity(String city) {
        scrollToElement(cityDropdown);
        jsClick(cityDropdown);
        WebElement input = driver.findElement(cityInput);
        input.sendKeys(city);
        input.sendKeys(Keys.ENTER);
    }

    public void clickSubmit() {
        scrollToElement(submitButton);
        jsClick(submitButton);
    }

    // Fill entire form
    public void fillForm(String firstName, String lastName, String email,
                         String gender, String mobile, String subject,
                         String hobby, String address) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        selectGender(gender);
        enterMobile(mobile);
        enterSubjects(subject);
        selectHobby(hobby);
        enterCurrentAddress(address);
    }

    // Verification methods
    public boolean isSubmissionSuccessful() {
        return isElementVisible(modalTitle);
    }

    public String getModalTitle() {
        return getText(modalTitle);
    }

    public String getModalBodyText() {
        return getText(modalBody);
    }

    public By getModalTitleLocator() {
        return modalTitle;
    }

    public By getModalBodyLocator() {
        return modalBody;
    }

    public void closeModal() {
        click(modalCloseButton);
    }

    // Check if form validation error is shown (required fields highlighted)
    public boolean isFirstNameInvalid() {
        WebElement element = driver.findElement(firstNameField);
        String cssClass = element.getAttribute("class");
        // When form is submitted with empty required fields, the field gets was-validated class
        return element.getCssValue("border-color").contains("rgb(220, 53, 69)");
    }

    public boolean isMobileInvalid() {
        WebElement element = driver.findElement(mobileField);
        return element.getCssValue("border-color").contains("rgb(220, 53, 69)");
    }
}
