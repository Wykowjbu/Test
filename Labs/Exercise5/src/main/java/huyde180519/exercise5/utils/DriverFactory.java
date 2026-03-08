package huyde180519.exercise5.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // KHÔNG tắt JavaScript vì DemoQA là trang React, cần JS để render
        // prefs.put("profile.managed_default_content_settings.javascript", 2);
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        return new ChromeDriver(options);
    }
}
