package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

public class BaseTest {

    protected static WebDriver driver;

    // Base URL of the MiniSocialNetwork ASP.NET application
    protected static final String baseUrl = "https://localhost:7009";

    @BeforeAll
    public static void setupClass() {
        driver = DriverFactory.getDriver();
    }

    @AfterAll
    public static void tearDown() {
        DriverFactory.quitDriver();
    }
}
