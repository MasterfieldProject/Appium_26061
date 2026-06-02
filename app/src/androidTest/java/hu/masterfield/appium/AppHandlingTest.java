package hu.masterfield.appium;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.appmanagement.ApplicationState;

public class AppHandlingTest {

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

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(PLATFORM)
                .setPlatformVersion(PLATFORM_VERSION)
                .setDeviceName(DEVICE_NAME)
                .setAutomationName(AUTOMATION)
                .setApp(APK_PATH)
                .setLanguage(LANG)
                .setLocale(LOCALE);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test() throws InterruptedException {
        boolean appInstalled = driver.isAppInstalled(APP_PACKAGE);
        System.out.println("Installed = " + appInstalled);

        System.out.println("Current package = " + driver.getCurrentPackage());
        System.out.println("Current activity = " + driver.currentActivity());

        ApplicationState appState = driver.queryAppState(APP_PACKAGE);
        System.out.println("App " + APP_PACKAGE + " state = " + appState);

        if (appState == ApplicationState.RUNNING_IN_FOREGROUND) {
            // terminate
            System.out.println("Terminate app");
            driver.terminateApp(APP_PACKAGE);

            appState = driver.queryAppState(APP_PACKAGE);
            System.out.println("App " + APP_PACKAGE + " state = " + appState);
        }

        Thread.sleep(2000);
        // activate
        System.out.println("Activate app");
        driver.activateApp(APP_PACKAGE);

        Thread.sleep(2000);
        appState = driver.queryAppState(APP_PACKAGE);
        System.out.println("App " + APP_PACKAGE + " state = " + appState);

        Thread.sleep(2000);
        // background
        System.out.println("Backgroud app");
        driver.runAppInBackground(Duration.ofSeconds(-1)); // background forever

        appState = driver.queryAppState(APP_PACKAGE);
        System.out.println("App " + APP_PACKAGE + " state = " + appState);

        // activate
        Thread.sleep(2000);
        System.out.println("Activate app");
        driver.activateApp(APP_PACKAGE);

        Thread.sleep(2000);
        appState = driver.queryAppState(APP_PACKAGE);
        System.out.println("App " + APP_PACKAGE + " state = " + appState);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
