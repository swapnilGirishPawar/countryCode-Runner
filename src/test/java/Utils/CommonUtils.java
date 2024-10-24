package Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
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
    public static Properties Prop;
    public static WebDriverWait wait;
    public static String iOSLocatorPropertiesFile = "./src/test/resources/iOSLocators.properties";
    public static String AndroidLocatorPropertiesFile = "./src/test/resources/AndroidLocators.properties";

    public static String ReadProperties(String Property, String Location) throws Throwable {
        Prop = new Properties();
        File FileLocation = new File(Location);
        FileReader ReadFile = new FileReader(FileLocation);
        Prop.load(ReadFile);
        return Prop.getProperty(Property);
    }

    public static void tapOnElementiOS(String element, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = ReadProperties(element, iOSLocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        if (value == null) {
            System.out.println("failed to click on " + elementName);
        } else {
            value.click();
        }
    }
    public static void tapOnElementAndroid(String element, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = ReadProperties(element, AndroidLocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        if (value == null) {
            System.out.println("failed to click on " + elementName);
        } else {
            value.click();
        }
    }

    public static void tapOnElement2(String element, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = element;
        WebElement value = StringToElementConverter(Locator, driver);
        if (value == null) {
            System.out.println("failed to click on " + elementName);
        } else {
            value.click();
        }
    }

    public static void typeiOS(String element, String input, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = ReadProperties(element, iOSLocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        if (value != null) {
            value.clear();
            value.sendKeys(input);
        } else {
            System.out.println("failed in type method");
        }
    }
    public static void typeAndroid(String element, String input, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = ReadProperties(element, AndroidLocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        if (value != null) {
            value.clear();
            value.sendKeys(input);
        } else {
            System.out.println("failed in type method");
        }
    }

    public String SelectProperCountryiOS(String CountryNameFromJson, AppiumDriver driver) {
        try {
            List<WebElement> countries = driver.findElements(By.xpath("//XCUIElementTypeSearchField[@name=\"Search\"]/ancestor::XCUIElementTypeOther/following-sibling::XCUIElementTypeCollectionView//XCUIElementTypeCell"));
            if (!countries.isEmpty()) {
                for (int i = 1; true; ) {
                    WebElement countryName = driver.findElement(By.xpath("//XCUIElementTypeSearchField[@name=\"Search\"]/ancestor::XCUIElementTypeOther/following-sibling::XCUIElementTypeCollectionView//XCUIElementTypeCell[" + i + "]/child::XCUIElementTypeOther[2]/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeStaticText"));
                    String ActualSelectedCountry = countryName.getAttribute("name");
                    if (ActualSelectedCountry.equalsIgnoreCase(CountryNameFromJson)) {
                        countryName.click();
                    }
                    return ActualSelectedCountry;
                }
            } else {
                System.out.println("No countries found");
            }
            return null;
        } catch (Exception e) {
            System.out.println("failed in GetListOfSearchedCountries method");
        }
        return null;
    }
    public String SelectProperCountryAndroid(String CountryNameFromJson, AppiumDriver driver) {

        try {
            WebElement element = driver.findElement(By.xpath("//android.widget.TextView[@text=\""+CountryNameFromJson+"\"]"));
            if(element.isDisplayed()){
             element.click();
             return CountryNameFromJson;
            }
             else {
                System.out.println("No countries found");
            }
            return null;
        } catch (Exception e) {
            System.out.println("failed in GetListOfSearchedCountries method");
        }
        return null;
    }

    protected static WebElement StringToElementConverter(String locator, AppiumDriver driver) throws Exception {
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
        By waitedElementLocation = wait(elementLocation, driver, 15);
        if (waitedElementLocation == null) {
            return null;
        } else {
            WebElement element = driver.findElement(waitedElementLocation);
            return element;
        }
    }

    public static By wait(By element, AppiumDriver driver, int sec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sec));
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            return element;
        } catch (Exception e) {
            System.out.println("element is not there");
            return null;
        }
    }

    public boolean validateString(String string1, String string2) {
        return string1.equalsIgnoreCase(string2);
    }

    public JsonArray readCountriesFromJson() throws Exception {
        FileReader reader = new FileReader("./src/test/resources/testphone.json");
        return JsonParser.parseReader(reader).getAsJsonArray();
    }

}
