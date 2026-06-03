package hu.masterfield.appium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Location;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.geolocation.AndroidGeoLocation;
import io.appium.java_client.android.options.UiAutomator2Options;

public class NavigationGoogleMapsTest {

    /* Appium params */
    public static final String APPIUM_URL = "http://localhost:4723";
    public static final String DEVICE_NAME = "device";
    public static final String PLATFORM = "Android";
    public static final String PLATFORM_VERSION = "15";
    public static final String AUTOMATION = "UiAutomator2";

    /* Screenshots */
    private static final String SCREENSHOT_FOLDER = "screens/";
    private static final String FILENAME_PREFIX = "location_scr_";
    private static final String FILENAME_SUFFIX = ".png";
    private static int seq = 1;

    /* Recording */
    private static final String REC_FOLDER = "recs/";
    private static final String REC_FILENAME_PREFIX = "rec_";
    private static final String REC_FILENAME_SUFFIX = ".mp4";
    private static int seqRec = 1;

    /* Language */
    public static final String LOCALE = "HU";
    public static final String LANG = "hu";
    // public static final String LOCALE = "US";
    // public static final String LANG = "en";

    /* App params */
    public static final String APP_PACKAGE = "com.google.android.apps.maps";
    public static final String LAUNCH_ACTIVITY = "com.google.android.maps.MapsActivity";

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
                .setAutoGrantPermissions(true)
                .setNoReset(true) // ha kézzel indítjuk
                .setFullReset(false); // ha kézzel indítjuk

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        Dimension size = driver.manage().window().getSize();
        System.out.println("Window height = " + size.getHeight());
        System.out.println("Window width = " + size.getWidth());

        Util.enablePointerPosition();
    }

    @Test
    public void test() throws InterruptedException {
        //wait.until(d -> d.findElement(AppiumBy.xpath("(//android.widget.Button)[2]"))).click();

        //wait.until(driver -> driver.findElements(AppiumBy.className("android.widget.Button"))).get(1).click();
        //String pageSource = driver.getPageSource();
        //System.out.println("Pagesource = " + pageSource);

//        Thread.sleep(15000);

        boolean locationServicesEnabled = driver.isLocationServicesEnabled();
        if (!locationServicesEnabled) {
            System.out.println("Enable location services");
            driver.toggleLocationServices();
        }

        Thread.sleep(5000);

        System.out.println("Set location to Parlament");
        AndroidGeoLocation parlamentLoc = new AndroidGeoLocation(47.50725469624803, 19.04539554042457);
        driver.setLocation(parlamentLoc);

        Location location = driver.getLocation();
        System.out.println("Location = " + location);

        WebElement map = wait.until(d -> d.findElement(AppiumBy.id("com.google.android.apps.maps:id/mainmap_container")));
        int leftX = map.getRect().getX();
        int topY = map.getRect().getY();

        int rightX = map.getRect().getX() + map.getRect().width;
        int bottomY = map.getRect().getY() + map.getRect().height;

        int centerX = leftX + map.getSize().width / 2;
        int centerY = topY + map.getSize().height / 2;

        System.out.println("leftX = " + leftX);
        System.out.println("topY = " + topY);
        System.out.println("rightX = " + rightX);
        System.out.println("bottomY = " + bottomY);
        System.out.println("centerX = " + centerX);
        System.out.println("centerY = " + centerY);

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        PointerInput.Origin origin = PointerInput.Origin.viewport();

        int startOffsetX = 0, endOffsetX = 200;
        int startOffsetY = 0, endOffsetY = 200;

        System.out.println("Zooming");
        Sequence zoom1 = new Sequence(finger1, 1);
        zoom1.addAction(finger1.createPointerMove(Duration.ZERO, origin, centerX + startOffsetX, centerY - startOffsetY));
        zoom1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        zoom1.addAction(new Pause(finger1, Duration.ofMillis(200)));
        zoom1.addAction(finger1.createPointerMove(Duration.ofMillis(300), origin, centerX - endOffsetX, centerY - endOffsetY));
        zoom1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence zoom2 = new Sequence(finger2, 1);
        zoom2.addAction(finger2.createPointerMove(Duration.ZERO, origin, centerX - startOffsetX, centerY + startOffsetY));
        zoom2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        zoom2.addAction(new Pause(finger2, Duration.ofMillis(200)));
        zoom2.addAction(finger2.createPointerMove(Duration.ofMillis(300), origin, centerX + endOffsetX, centerY + endOffsetY));
        zoom2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(zoom1, zoom2));
        System.out.println("Zoom done");

        Thread.sleep(5000);

        System.out.println("Pinching");
        Sequence pinch1 = new Sequence(finger1, 1);
        pinch1.addAction(finger1.createPointerMove(Duration.ZERO, origin, centerX - 300, centerY - 300));
        pinch1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        //zoom1.addAction(new Pause(finger1, Duration.ofMillis(200)));
        pinch1.addAction(finger1.createPointerMove(Duration.ofMillis(300), origin, centerX - 100, centerY - 100));
        pinch1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence pinch2 = new Sequence(finger2, 1);
        pinch2.addAction(finger2.createPointerMove(Duration.ZERO, origin, centerX + 300, centerY + 300));
        pinch2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        //zoom2.addAction(new Pause(finger2, Duration.ofMillis(200)));
        pinch2.addAction(finger2.createPointerMove(Duration.ofMillis(300), origin, centerX + 100, centerY + 100));
        pinch2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(pinch1, pinch2));
        System.out.println("Pinch done");

        System.out.println("Rotating");
        Sequence rotate1 = new Sequence(finger1, 1);
        rotate1.addAction(finger1.createPointerMove(Duration.ZERO, origin, centerX - 300, centerY - 300));
        rotate1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        //zoom1.addAction(new Pause(finger1, Duration.ofMillis(200)));
        rotate1.addAction(finger1.createPointerMove(Duration.ofMillis(300), origin, centerX + 300, centerY - 300));
        rotate1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence rotate2 = new Sequence(finger2, 1);
        rotate2.addAction(finger2.createPointerMove(Duration.ZERO, origin, centerX + 300, centerY + 300));
        rotate2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        //zoom2.addAction(new Pause(finger2, Duration.ofMillis(200)));
        rotate2.addAction(finger2.createPointerMove(Duration.ofMillis(300), origin, centerX - 300, centerY + 300));
        rotate2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(rotate1, rotate2));
        System.out.println("Rotate done");
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

    private void takeScreenshot() {
        File file = driver.getScreenshotAs(OutputType.FILE);
        String filename = SCREENSHOT_FOLDER + FILENAME_PREFIX + (seq++) + FILENAME_SUFFIX;
        System.out.println("Taking screenshot to " + filename);

        try {
            FileUtils.copyFile(file, new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveRecording() {
        String filename = REC_FOLDER + REC_FILENAME_PREFIX + (seqRec++) + REC_FILENAME_SUFFIX;

        System.out.println("Stop screen recording. Save file to " + filename);

        String video = driver.stopRecordingScreen();
        byte[] decode = Base64.getDecoder().decode(video);

        try {
            FileUtils.writeByteArrayToFile(new File(filename), decode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
