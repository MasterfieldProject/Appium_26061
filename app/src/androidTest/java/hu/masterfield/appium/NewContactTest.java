package hu.masterfield.appium;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
                .setLocale(LOCALE);
        driver = new AndroidDriver(new URL(APPIUM_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void test() throws InterruptedException {
        wait.until(d -> d.findElement(AppiumBy.id("com.google.android.dialer:id/tab_contacts"))).click();

        WebElement newContactButton;
        try {
            newContactButton = driver.findElement(AppiumBy.id("com.google.android.dialer:id/empty_content_view_action"));
        } catch (NoSuchElementException ex) {
            newContactButton = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.google.android.dialer:id/contact_name\" and @text=\"Új névjegy\"]"));
        }
        newContactButton.click();

        WebElement firstname = wait.until(d -> d.findElement(By.xpath("//android.widget.EditText[@text='Utónév']")));
        firstname.sendKeys("Master");

        WebElement lastname = driver.findElement(By.xpath("//android.widget.EditText[@text='Családnév']"));
        lastname.sendKeys("Field");

        WebElement company = driver.findElement(By.xpath("//android.widget.EditText[@text='Cég']"));
        company.sendKeys("Masterfield");

        WebElement phone = driver.findElement(By.xpath("//android.widget.EditText[@text='Telefon']"));
        phone.sendKeys("06301234567");

        WebElement saveButton = driver.findElement(AppiumBy.id("com.google.android.contacts:id/toolbar_button"));
        saveButton.click();

        List<WebElement> contacts = wait.until(d -> d.findElements(AppiumBy.id("com.google.android.dialer:id/contact_name")));
        assertFalse(contacts.isEmpty());
        for (WebElement cont : contacts) {
            System.out.println(cont.getText());
            assertTrue(cont.getText().length() > 0);
        }

    }

    @Test
    public void testDeleteContacts() throws InterruptedException {
        WebElement contactsButton = driver.findElement(AppiumBy.id("com.google.android.dialer:id/tab_contacts"));
        contactsButton.click();

        Thread.sleep(3000);

        List<WebElement> contacts;
        do {
            contacts = driver.findElements(AppiumBy.xpath("(//*[@resource-id='com.google.android.dialer:id/contact_name'])[2]"));
            contacts.get(0).click();
            Thread.sleep(3000);
            WebElement menu = driver.findElement(AppiumBy.id("com.google.android.contacts:id/action_bar_overflow_menu"));
            menu.click();

            Thread.sleep(1000);
            WebElement delete = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.google.android.contacts:id/title\" and @text=\"Törlés\"]"));
            delete.click();

            Thread.sleep(1000);

            WebElement deleteAccept = driver.findElement(AppiumBy.id("android:id/button1"));
            deleteAccept.click();

            Thread.sleep(1000);
            contacts = driver.findElements(AppiumBy.xpath("(//*[@resource-id='com.google.android.dialer:id/contact_name'])[2]"));
        } while (contacts != null && contacts.size() > 0);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
