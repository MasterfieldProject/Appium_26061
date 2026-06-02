package hu.masterfield.appium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;

import io.appium.java_client.Location;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.geolocation.AndroidGeoLocation;
import io.appium.java_client.android.options.UiAutomator2Options;

public class LocationTest {

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

        System.out.println("Start recording");
        driver.startRecordingScreen();
        /*
        driver.startRecordingScreen(new AndroidStartScreenRecordingOptions().withVideoSize("1280X720")
                .withTimeLimit(Duration.ofMinutes(2))
                .enableBugReport());

         */

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

        Thread.sleep(15000);
        takeScreenshot();

        System.out.println("Set location to Bazilika");
        AndroidGeoLocation bazilikaLoc = new AndroidGeoLocation(47.79921802407771, 18.73653542707597);
        driver.setLocation(bazilikaLoc);

        Thread.sleep(15000);
        takeScreenshot();

        System.out.println("Set location to Dzsámi");
        AndroidGeoLocation dzsamiLoc = new AndroidGeoLocation(46.0773172931036, 18.227779336824696); // Dzsámi Pécs
        driver.setLocation(dzsamiLoc);

        Thread.sleep(15000);
        takeScreenshot();
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
        saveRecording();
        driver.quit();
    }
}
