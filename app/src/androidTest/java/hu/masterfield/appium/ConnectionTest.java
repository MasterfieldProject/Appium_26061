package hu.masterfield.appium;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.options.UiAutomator2Options;

public class ConnectionTest {

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
                .setAutomationName(AUTOMATION);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test() throws InterruptedException {
        driver.openNotifications();

        printConnectionState();

        Thread.sleep(2000);

        // wifi
        System.out.println("Toggle wifi");
        driver.toggleWifi();

        printConnectionState();
        Thread.sleep(2000);

        driver.toggleAirplaneMode();

        Thread.sleep(2000);
        printConnectionState();

        // minden kikapcsolva
        /*
        0 (None) 	        0 	0 	0
        1 (Airplane Mode) 	0 	0 	1
        2 (Wifi only) 	    0 	1 	0
        4 (Data only) 	    1 	0 	0
        6 (All network on) 	1 	1 	0
         */
        System.out.println("**********************************");
        System.out.println("Set connection to None");
        ConnectionState cs = new ConnectionState(0);
        driver.setConnection(cs);

        Thread.sleep(5000);

        printConnectionState();

        // airplane mode
        System.out.println("**********************************");
        System.out.println("Set connection to Airplane mode");
        cs = new ConnectionState(1);
        driver.setConnection(cs);

        Thread.sleep(5000);

        printConnectionState();


        // wifi only
        System.out.println("**********************************");
        System.out.println("Set connection to Wifi only");
        cs = new ConnectionState(2);
        driver.setConnection(cs);

        Thread.sleep(5000);

        printConnectionState();

        // data only
        System.out.println("**********************************");
        System.out.println("Set connection to Data only");
        cs = new ConnectionState(4);
        driver.setConnection(cs);

        Thread.sleep(5000);

        printConnectionState();

        // all on
        System.out.println("**********************************");
        System.out.println("Set connection to All on");
        cs = new ConnectionState(6);
        driver.setConnection(cs);

        Thread.sleep(5000);

        printConnectionState();

    }

    private void printConnectionState() {
        ConnectionState connectionState = driver.getConnection();
        System.out.println("Wifi enabled = " + connectionState.isWiFiEnabled());
        System.out.println("Data enabled = " + connectionState.isDataEnabled());
        System.out.println("Airplane mode enabled = " + connectionState.isAirplaneModeEnabled());
        System.out.println("Bitmask = " + connectionState.getBitMask());
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
