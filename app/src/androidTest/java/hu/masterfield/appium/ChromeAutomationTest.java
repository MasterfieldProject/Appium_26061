package hu.masterfield.appium;

import static java.time.Duration.ofMillis;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Set;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;

public class ChromeAutomationTest {

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
    public static final String APP_PACKAGE = "com.android.chrome";
    public static final String APP_ACTIVITY = "com.google.android.apps.chrome.Main";

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
                .setAppActivity(APP_ACTIVITY)
                .setLanguage(LANG)
                .setLocale(LOCALE)
                .setNoReset(false)
                .setFullReset(false);
        // options.withBrowserName("Chrome");
        options.setCapability(
                "appium:chromedriverExecutable",
                "D:\\dev\\training\\MFAPPIUM\\project\\Appium_26061\\app\\chromedriver\\chromedriver.exe");

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test2() throws InterruptedException {
        driver.get("https://hwsw.hu");

        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/signin_fre_dismiss_button"))).click();
        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/negative_button"))).click();

        Thread.sleep(10000);

        Set<String> contextHandles = driver.getContextHandles();
        System.out.println("Handles = " + contextHandles);

        driver.context("NATIVE_APP");
        //driver.context("WEBVIEW_chrome");

        System.out.println("Scroll vertical");
        for (int i = 0; i < 4; i++) {
            int x = 400, y = 800;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence seq = new Sequence(finger, 1);
            seq.addAction(finger.createPointerMove(ofMillis(0), PointerInput.Origin.viewport(), x, y));
            seq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            seq.addAction(finger.createPointerMove(ofMillis(400), PointerInput.Origin.viewport(), x, y - 800));
            seq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(seq));
        }
    }

    @Test
    public void test3() throws InterruptedException {
        driver.get("https://hwsw.hu");

        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/signin_fre_dismiss_button"))).click();
        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/negative_button"))).click();

        Thread.sleep(10000);

        Set<String> contextHandles = driver.getContextHandles();
        System.out.println("Handles = " + contextHandles);

        driver.context("NATIVE_APP");

        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/tab_switcher_button"))).click();

        Thread.sleep(2000);

        driver.pressKey(new KeyEvent(AndroidKey.BACK));

        Thread.sleep(2000);

        driver.context("WEBVIEW_chrome");

        //System.out.println(driver.getPageSource());
        wait.until(d -> d.findElement(By.className("mobile-menu-trigger"))).click();

        wait.until(d -> d.findElement(By.linkText("TESZTEK"))).click();

        Thread.sleep(2000);

        driver.navigate().back();

        wait.until(d -> d.findElement(By.linkText("VIDEÓK"))).click();

        Thread.sleep(2000);

        driver.navigate().back();
    }

    @Test
    public void test() throws InterruptedException {
        driver.get("https://hwsw.hu");

        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/signin_fre_dismiss_button"))).click();
        /*
        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/no_button"))).click();
        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/more_button"))).click();
        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/ack_button"))).click();
        */
        wait.until(d -> d.findElement(AppiumBy.id("com.android.chrome:id/negative_button"))).click();

        Thread.sleep(10000);

        Set<String> contextHandles = driver.getContextHandles();
        System.out.println("Handles = " + contextHandles);

        driver.context("WEBVIEW_chrome");

        //System.out.println(driver.getPageSource());
        wait.until(d -> d.findElement(By.className("mobile-menu-trigger"))).click();

        wait.until(d -> d.findElement(By.linkText("TESZTEK"))).click();

        Thread.sleep(2000);

        driver.navigate().back();

        wait.until(d -> d.findElement(By.linkText("VIDEÓK"))).click();

        Thread.sleep(2000);

        driver.navigate().back();
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
        Util.disablePointerPosition();
        driver.quit();

    }
}
