package hu.masterfield.appium;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class StartActivityTest {

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
    public static final String APP_PACKAGE = "com.google.android.deskclock";
    public static final String LAUNCH_ACTIVITY = "com.android.deskclock.DeskClock";

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(PLATFORM)
                .setPlatformVersion(PLATFORM_VERSION)
                .setDeviceName(DEVICE_NAME)
                .setAutomationName(AUTOMATION)
                .setLanguage(LANG)
                .setLocale(LOCALE);

        options.setCapability("appium:autoLaunch", false);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test() throws InterruptedException {
        // app indítása
        Map<String, String> args = new HashMap<>();
        args.put("intent", APP_PACKAGE + "/" + LAUNCH_ACTIVITY);
        args.put("package", APP_PACKAGE);
        driver.executeScript("mobile:startActivity", args);

        wait.until(driver -> driver.findElement(AppiumBy.id("com.google.android.deskclock:id/tab_menu_alarm"))).click();

        Thread.sleep(1000);

        wait.until(driver -> driver.findElement(AppiumBy.id("com.google.android.deskclock:id/tab_menu_clock"))).click();
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
