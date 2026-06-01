package hu.masterfield;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class NewContactTest {

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
    public static final String APP_PACKAGE = "com.google.android.dialer";
    public static final String LAUNCH_ACTIVITY = "com.android.dialer.main.impl.MainActivity";

    private AndroidDriver driver;

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
        driver = new AndroidDriver(new URL(APPIUM_URL), options);
    }

    @Test
    public void test() throws InterruptedException {
        WebElement contactsButton = driver.findElement(AppiumBy.id("com.google.android.dialer:id/tab_contacts"));
        contactsButton.click();

        Thread.sleep(3000);

        WebElement newContactButton = driver.findElement(AppiumBy.id("com.google.android.dialer:id/empty_content_view_action"));
        newContactButton.click();
        Thread.sleep(3000);

        WebElement firstname = driver.findElement(By.xpath("//android.widget.EditText[@text='Utónév']"));
        firstname.sendKeys("Dani");

        WebElement lastname = driver.findElement(By.xpath("//android.widget.EditText[@text='Családnév']"));
        lastname.sendKeys("Markó");

        WebElement company = driver.findElement(By.xpath("//android.widget.EditText[@text='Cég']"));
        company.sendKeys("Szerencsejáték Zrt.");

        WebElement phone = driver.findElement(By.xpath("//android.widget.EditText[@text='Telefon']"));
        phone.sendKeys("06302345678");

        WebElement saveButton = driver.findElement(AppiumBy.id("com.google.android.contacts:id/toolbar_button"));
        saveButton.click();

    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
