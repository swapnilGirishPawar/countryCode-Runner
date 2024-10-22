package Utils;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class CommonUtils {
    public static AndroidDriver androidDriver;
    public static IOSDriver iosDriver;
    public static Properties Prop;
    public static String LocatorPropertiesFile = "./src/test/resources/Locator.properties";
    public static String AppConfigFilePath = "./src/test/resources/app.properties";
    public static String LocatorPropertiesFile3 = "./src/test/resources/Login.properties";

    public static String ReadProperties(String Property, String Location) throws Throwable {
        Prop = new Properties();
        File FileLocation = new File(Location);
        FileReader ReadFile = new FileReader(FileLocation);
        Prop.load(ReadFile);
        return Prop.getProperty(Property);
    }
    public static void tapOnElement(String element, String elementName) throws Throwable {
        String Locator = ReadProperties(element, LocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator);
        if (value == null) {
            System.out.println("failed");
        } else {
            value.click();
            System.out.println(("Clicked on [" + elementName + "]"));
        }
    }
    public static void type(String element, String input, String elementName) throws Throwable {
        String Locator = ReadProperties(element, LocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator);
        if (value != null) {
            value.clear();
            value.sendKeys(input);
            System.out.println(("Typed [" + input + "] on the " + elementName));
        } else {
            System.out.println("failed in type method");
        }
    }

    public List GetListOfSearchedCountries(){
        return null;
    }

    private static WebElement StringToElementConverter(String locator) throws Exception {
        String LocatorType = locator.split("\\$")[0];
        String LocatorValue = locator.split("\\$")[1];
        By elementLocation;
        if (LocatorType.equalsIgnoreCase("xpath")) {
            elementLocation = By.xpath(LocatorValue);
        } else if (LocatorType.equalsIgnoreCase("id")) {
            elementLocation = By.id(LocatorValue);
        } else if (LocatorType.equalsIgnoreCase("accessibilityid")) {
            elementLocation = MobileBy.accessibilityId(LocatorValue);
        } else {
            throw new Exception("Unknown locator type '" + LocatorType + "'");
        }
        By waitedElementLocation = wait(elementLocation, iosDriver, 15);
        if (waitedElementLocation == null) {
            return null;
        } else {
            WebElement element = iosDriver.findElement(waitedElementLocation);
            return element;
        }
    }
    public static By wait(By element, IOSDriver driver, int sec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sec));
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            return element;
        } catch (Exception e) {
            System.out.println("element is not there");
            return null;
        }
    }
}
