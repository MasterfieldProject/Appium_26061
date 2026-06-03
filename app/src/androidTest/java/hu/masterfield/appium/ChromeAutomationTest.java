package hu.masterfield.appium;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
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
    public static final String APP_PACKAGE = "com.example.drawingapp";
    public static final String APP_ACTIVITY = ".MainActivity";

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
                .setNoReset(false);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        Util.enablePointerPosition();
    }

    @Test
    public void test() throws InterruptedException {
        WebElement view = wait.until(d -> d.findElement(AppiumBy.id("com.example.drawingapp:id/drawingView")));
        Rectangle viewRect = view.getRect();
        System.out.println("x = " + viewRect.x);
        System.out.println("y = " + viewRect.y);
        System.out.println("height = " + viewRect.height);
        System.out.println("width = " + viewRect.width);

        int squareSize = 300;
        int centerX = viewRect.x + viewRect.width / 2;
        int centerY = viewRect.y + viewRect.height / 2;

        int leftX = centerX - squareSize / 2;
        int rightX = centerX + squareSize / 2;
        int topY = centerY - squareSize / 2;
        int bottomY = centerY + squareSize / 2;

        int duration = 10;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        PointerInput.Origin origin = PointerInput.Origin.viewport();

        Sequence actions = new Sequence(finger, 1);
        actions.addAction(finger.createPointerMove(Duration.ZERO, origin, leftX, topY));
        actions.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        actions.addAction(finger.createPointerMove(Duration.ofMillis(duration), origin, rightX, topY));
        actions.addAction(finger.createPointerMove(Duration.ofMillis(duration), origin, rightX, bottomY));
        actions.addAction(finger.createPointerMove(Duration.ofMillis(duration), origin, leftX, bottomY));
        actions.addAction(finger.createPointerMove(Duration.ofMillis(duration), origin, leftX, topY));

        actions.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(actions));

        Thread.sleep(5000);
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
