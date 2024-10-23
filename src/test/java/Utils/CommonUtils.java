package Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import org.apache.poi.ss.usermodel.CellStyle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;

public class CommonUtils {
//    public static String countryName;
    public static Properties Prop;
    public static WebDriverWait wait;
    public static String LocatorPropertiesFile = "./src/test/resources/iOSLocators.properties";

    public static String ReadProperties(String Property, String Location) throws Throwable {
        Prop = new Properties();
        File FileLocation = new File(Location);
        FileReader ReadFile = new FileReader(FileLocation);
        Prop.load(ReadFile);
        return Prop.getProperty(Property);
    }

    public static void tapOnElement(String element, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = ReadProperties(element, LocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        if (value == null) {
            System.out.println("failed");
        } else {
            value.click();
            System.out.println(("Clicked on [" + elementName + "]"));
        }
    }
    public static void tapOnElement2(String element, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = element;
        WebElement value = StringToElementConverter(Locator, driver);
        if (value == null) {
            System.out.println("failed");
        } else {
            value.click();
            System.out.println(("Clicked on [" + elementName + "]"));
        }
    }

    public static void type(String element, String input, String elementName, AppiumDriver driver) throws Throwable {
        String Locator = ReadProperties(element, LocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        if (value != null) {
            value.clear();
            value.sendKeys(input);
            System.out.println(("Typed [" + input + "] on the " + elementName));
        } else {
            System.out.println("failed in type method");
        }
    }

    public String SelectProperCountry(String CountryNameFromJson, AppiumDriver driver) {
        try {
            List<WebElement> countries = driver.findElements(By.xpath("//XCUIElementTypeSearchField[@name=\"Search\"]/ancestor::XCUIElementTypeOther/following-sibling::XCUIElementTypeCollectionView//XCUIElementTypeCell"));
            if (!countries.isEmpty()) {
                System.out.println("Countries found");
                for (int i = 1; i <= countries.size(); i++) {
                    System.out.println(i + " " + "country size " + countries.size());
                    WebElement countryName = driver.findElement(By.xpath("//XCUIElementTypeSearchField[@name=\"Search\"]/ancestor::XCUIElementTypeOther/following-sibling::XCUIElementTypeCollectionView//XCUIElementTypeCell["+i+"]/child::XCUIElementTypeOther[2]/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeStaticText"));
                    System.out.println(countryName.getAttribute("name"));
                    if(countryName.getAttribute("name").equalsIgnoreCase(CountryNameFromJson)){
                        System.out.println("here");
                        countryName.click();
                        return countryName.getAttribute("name");
                    }
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

    private static WebElement StringToElementConverter(String locator, AppiumDriver driver) throws Exception {
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


    public static WebElement visibilityOfElement(String element, IOSDriver driver) throws Throwable {
        String Locator = ReadProperties(element, LocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOf(value));
        return value;
    }

//    public static void jsonReader() {
//        try {
//            // Read the JSON file
//            FileReader reader = new FileReader("./src/test/resources/phonenumber.json");
//            char[] buffer = new char[1024];
//            int read = 0;
//            StringBuilder content = new StringBuilder();
//            while ((read = reader.read(buffer)) != -1) {
//                content.append(buffer, 0, read);
//            }
//            reader.close();
//
//            // Create a JSONArray from the string
//            JSONArray jsonArray = new JSONArray(content.toString());
//
//            // Iterate over each object in the array
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                // Extract the name
//                countryName = jsonObject.getString("name");
//
//                // Do something with the name
//                System.out.println("Name: " + countryName);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean validateString(String string1, String string2) {
        return string1.equalsIgnoreCase(string2);
    }

}
