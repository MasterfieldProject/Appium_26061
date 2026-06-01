package hu.masterfield.appium;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class WaitTest {

    /* Appium params */
    public static final String APPIUM_URL = "http://localhost:4723";
    public static final String DEVICE_NAME = "device";
    public static final String PLATFORM = "Android";
    public static final String PLATFORM_VERSION = "15";
    public static final String AUTOMATION = "UiAutomator2";

    /* Language */
    public static final String LOCALE = "HU";
    public static final String LANG = "hu";
    // public static final String LOCALE = "US";
    // public static final String LANG = "en";

    /* App params */
    public static final String APP_PACKAGE = "io.appium.android.apis";
    public static final String LAUNCH_ACTIVITY = ".view.ProgressBar3";

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(PLATFORM)
                .setPlatformVersion(PLATFORM_VERSION)
                .setDeviceName(DEVICE_NAME)
                .setAutomationName(AUTOMATION)
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(LAUNCH_ACTIVITY)
                .setLanguage(LANG)
                .setLocale(LOCALE);
        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void test() throws InterruptedException {
        //wait.until(driver -> driver.findElement(AppiumBy.accessibilityId("Views"))).click();
        //wait.until(driver -> driver.findElement(AppiumBy.accessibilityId("Progress Bar"))).click();

        try {
            driver.findElement(AppiumBy.id("android:id/alertTitle"));
        } catch (NoSuchElementException e) {
            System.out.println("Az elem nem található!");
        }

        // wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // lokálisan is beállítható

        wait.until(driver -> driver.findElement(AppiumBy.id("android:id/alertTitle"))).click(); // visibility

        System.out.println("Az elem látható!");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(AppiumBy.id("android:id/alertTitle")));

        System.out.println("Az elem nem látható!");
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
