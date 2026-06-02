package hu.masterfield.appium;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class ResetAppTest {

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
    public static final String APK_PATH = System.getProperty("user.dir") + "/apks/ApiDemos-debug.apk";
    public static final String LAUNCH_ACTIVITY = ".ApiDemos";
    public static final String WAIT_ACTIVITY = ".view.ProgressBar3";

    private AndroidDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(PLATFORM)
                .setPlatformVersion(PLATFORM_VERSION)
                .setDeviceName(DEVICE_NAME)
                .setAutomationName(AUTOMATION)
                .setApp(APK_PATH)
                .setAppWaitPackage(APP_PACKAGE)
                .setAppWaitActivity(WAIT_ACTIVITY)
                .setNoReset(false)
                .setFullReset(false)
                .setLanguage(LANG)
                .setLocale(LOCALE);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
    }

    @Test
    public void test() throws InterruptedException {
        boolean appInstalled = driver.isAppInstalled(APP_PACKAGE);
        System.out.println("Installed = " + appInstalled);

        System.out.println("Current package = " + driver.getCurrentPackage());
        System.out.println("Current activity = " + driver.currentActivity());
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
