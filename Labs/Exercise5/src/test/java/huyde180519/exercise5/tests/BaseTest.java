package huyde180519.exercise5.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import huyde180519.exercise5.utils.DriverFactory;

public abstract class BaseTest {
    protected static WebDriver driver;

    @BeforeAll
    public static void setUpBase() {
        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
    }

    @AfterAll
    public static void tearDownBase() {
        if (driver != null) {
            driver.quit();
        }
    }
}
