package hu.masterfield.appium;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;

public class KeysTest {

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
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(LAUNCH_ACTIVITY)
                .setLanguage(LANG)
                .setLocale(LOCALE);

        options.setCapability("appium:allowInsecure", List.of("adb_shell"));

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        /*
        driver.executeScript(
                "mobile: shell",
                Map.of(
                        "command", "settings",
                        "args", List.of("put", "global", "auto_time", "0")
                )
        );
        driver.executeScript("mobile: shell", Map.of(
                "command", "date",
                "args", List.of("20260602.114500")
        ));
         */
    }

    @Test
    public void test() throws InterruptedException {
        wait.until(d -> d.findElement(AppiumBy.id("com.google.android.deskclock:id/tab_menu_alarm"))).click();

        wait.until(d -> d.findElement(AppiumBy.id("com.google.android.deskclock:id/fab"))).click();

        wait.until(d -> d.findElement(AppiumBy.id("com.google.android.deskclock:id/material_timepicker_mode_button"))).click();

        String deviceTime = driver.getDeviceTime();
        System.out.println("Device time = " + deviceTime);

        DateTimeFormatter df = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        LocalDateTime dt = LocalDateTime.parse(deviceTime, df).plusMinutes(2);

        String hours = "" + dt.getHour();
        String minutes = "" + dt.getMinute();

        for (char c : hours.toCharArray()) {
            driver.pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c))); // DIGIT_1, DIGIT_2
        }

        for (char c : minutes.toCharArray()) {
            driver.pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c))); // DIGIT_1, DIGIT_2
        }

        wait.until(d -> d.findElement(AppiumBy.id("com.google.android.deskclock:id/material_timepicker_ok_button")));
    }

    private void toggleLock() {
        boolean deviceLocked = driver.isDeviceLocked();
        System.out.println("Locked = " + deviceLocked);

        if (deviceLocked) {
            System.out.println("Unlocking");
            driver.unlockDevice();
        } else {
            System.out.println("Locking");
            driver.lockDevice();
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
