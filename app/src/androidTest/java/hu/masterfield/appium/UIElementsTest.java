package hu.masterfield.appium;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class UIElementsTest {

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
    public static final String LAUNCH_ACTIVITY = "io.appium.android.apis.view.Controls1";

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
                .setLocale(LOCALE)
                .setNoReset(true);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test() throws InterruptedException {
        // editText
        WebElement editText = wait.until(d -> d.findElement(AppiumBy.id("io.appium.android.apis:id/edit")));
        editText.sendKeys("Masterfield");
        assertEquals(editText.getText(), "Masterfield");
        Thread.sleep(5000);

        // checkbox
        WebElement checkbox = wait.until(d -> d.findElement(AppiumBy.id("io.appium.android.apis:id/check1")));
        System.out.println("Checkbox checked = " + checkbox.getAttribute("checked"));
        checkbox.click();
        System.out.println("Checkbox checked = " + checkbox.getAttribute("checked"));

        // togglebutton
        WebElement toggleButton = wait.until(d -> d.findElement(AppiumBy.id("io.appium.android.apis:id/toggle1")));
        System.out.println("toggleButton checked = " + toggleButton.getAttribute("checked"));
        toggleButton.click();
        System.out.println("toggleButton checked = " + toggleButton.getAttribute("checked"));

        // spinner
        WebElement spinner = wait.until(d -> d.findElement(AppiumBy.id("io.appium.android.apis:id/spinner1")));
        spinner.click();

        // ebben az esetben az első elem megjelenése után tovább engedi
        // List<WebElement> options = wait.until(d -> d.findElements(AppiumBy.className("android.widget.CheckedTextView")));

        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.className("android.widget.CheckedTextView")));
        options.get(2).click();

        WebElement spinnerText = wait.until(d -> d.findElement(AppiumBy.id("android:id/text1")));
        assertEquals(spinnerText.getText(), "Earth");

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
