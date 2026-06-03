package hu.masterfield.appium;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebElement;
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
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;

public class TescoTest {

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
    public static final String APP_PACKAGE = "hu.bitnet.tesco";

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
                .setAutoGrantPermissions(true)
                .setLanguage(LANG)
                .setLocale(LOCALE)
                .setNoReset(true);

        options.setCapability("appium:forceAppLaunch", true);

        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test1() throws InterruptedException {
        WebElement homeTextView = wait.until(d -> driver.findElement(AppiumBy.id("hu.bitnet.tesco:id/home_tesco_text")));
        assertEquals(homeTextView.getText(), "Üdvözöljük.");

        WebElement bottomNavigationView = wait.until(d -> driver.findElement(AppiumBy.id("hu.bitnet.tesco:id/bottom_navigation_view")));
        List<WebElement> itemBottomNavigationList = bottomNavigationView.findElements(AppiumBy.className("android.widget.TextView"));
        assertTrue(itemBottomNavigationList.size() == 5);
        assertEquals(itemBottomNavigationList.get(0).getText(), "Főoldal");
        assertEquals(itemBottomNavigationList.get(1).getText(), "Kedvencek");
        assertEquals(itemBottomNavigationList.get(2).getText(), "Keresés");
        assertEquals(itemBottomNavigationList.get(3).getText(), "Rendelések");
        assertEquals(itemBottomNavigationList.get(4).getText(), "Kosár");
    }

    @Test
    public void test2() throws InterruptedException {
        WebElement homeTextView = wait.until(d -> driver.findElement(AppiumBy.id("hu.bitnet.tesco:id/home_tesco_text")));
        assertEquals(homeTextView.getText(), "Üdvözöljük.");

        // navigate to search screen
        WebElement bottomNavigationView = wait.until(d -> driver.findElement(AppiumBy.id("hu.bitnet.tesco:id/bottom_navigation_view")));
        List<WebElement> itemBottomNavigationList = bottomNavigationView.findElements(AppiumBy.className("android.widget.TextView"));
        itemBottomNavigationList.get(2).click();

        WebElement searchInputField = wait.until(d -> driver.findElement(AppiumBy.id("hu.bitnet.tesco:id/search_input_field")));
        searchInputField.sendKeys("monster");
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));

        WebElement productCountTextView = wait.until(d -> d.findElement(AppiumBy.xpath("//android.view.View[contains(@content-desc,'Product count')]/android.widget.TextView")));
        String countText = productCountTextView.getText();

        System.out.println("DEBUG countText = " + countText);
        assertEquals(countText, "26 termék");

        wait.until(d -> d.findElement(AppiumBy.xpath("//*[@resource-id='hu.bitnet.tesco:id/plp_list_dynamic_filter_layout']//android.widget.Button[1]"))).click();

        wait.until(d -> d.findElement(AppiumBy.xpath("//android.widget.LinearLayout[@resource-id='hu.bitnet.tesco:id/additional_filter_container'])[1]"))).click();
    }

    @AfterTest
    public void tearDown() {
        Util.disablePointerPosition();
        driver.quit();

    }
}
