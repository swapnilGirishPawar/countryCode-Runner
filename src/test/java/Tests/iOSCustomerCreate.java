package Tests;

import BaseClass.Capabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class iOSCustomerCreate extends Capabilities {
    String customerTab;
    String addNewCustomer;
    String addCustomerManually;
    String customerName;
    String phoneNumber;
    String saveButton;
    String customerSearchBar;
    String backBtn;
    String calendarBtn;
    String cancelBtn;
    String discardNleave;
    String clearSearchedQuery;
    String cancelSearch;



    public void setup() throws Throwable {
        driver = (IOSDriver) launchDriver("iOS");
    }

    public void beforeTest() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("src/test/java/Utils/iOSLocators.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerTab = properties.getProperty("customerTab");
        addNewCustomer = properties.getProperty("addNewCustomer");
        addCustomerManually = properties.getProperty("addCustomerManually");
        customerName = properties.getProperty("customerName");
        phoneNumber = properties.getProperty("customerPhonenumber");
        saveButton = properties.getProperty("saveButton");
        customerSearchBar = properties.getProperty("customerSearchBar");
        backBtn = properties.getProperty("backBtn");
        calendarBtn = properties.getProperty("calendarBtn");
        cancelBtn = properties.getProperty("cancelBtn");
        discardNleave = properties.getProperty("discardNleave");
        clearSearchedQuery = properties.getProperty("clearSearchedQuery");
        cancelSearch = properties.getProperty("cancelSearch");

    }


    public String customerFlow(String countryName, String phoneNumber, String dialingCode, AppiumDriver driver) throws Throwable {
        beforeTest();
        String FailedCountry = createCustomer(countryName, phoneNumber);
        if(FailedCountry == null){
            openCustomerOverview(countryName);
            validatePhoneNumber(driver, dialingCode, phoneNumber);
        }
        return FailedCountry;

    }

    public String createCustomer(String countryName, String mobileNumber) throws Exception {
        clickElementByXPath(customerTab);
        clickElementByXPath(addNewCustomer);
        clickElementByXPath(addCustomerManually);
        sendKeysByXpath(customerName, countryName);
        sendKeysByXpath(phoneNumber, mobileNumber);
        clickElementByXPath(saveButton);
        Thread.sleep(2000);

        if (isDisplayed(saveButton)) {
            System.out.println("Customer created Failed!!");
            exitCustomerCreate();
            return countryName;
        } else {
            System.out.println("Customer created Successfully!!");
            gobackToCalendar();
        }
        return null;
    }

    public void openCustomerOverview(String customerName) throws Exception {
        clickElementByXPath(customerTab);
        sendKeysByXpath(customerSearchBar, customerName);
        Thread.sleep(2000);
        driver.findElement(By.id(customerName)).click();
    }

    public void exitCustomerCreate() throws Exception {
        clickElementByXPath(cancelBtn);
        clickElementByXPath(discardNleave);
        clickElementByXPath(calendarBtn);
    }

    public void gobackToCalendar() throws Exception {
        clickElementByXPath(backBtn);
        clickElementByXPath(calendarBtn);
    }

    public void validatePhoneNumber(AppiumDriver driver, String dialingCode, String phoneNumber) throws Throwable {
        String FullNumber = "+" +dialingCode + phoneNumber;
        String phoneNumberOverView = getTextOfElement(driver, "General.phoneNumberOverview").replaceAll("\\s", "");
        if(phoneNumberOverView.equalsIgnoreCase(FullNumber)){
//        if (isDisplayed("//XCUIElementTypeButton[@name=\"" + FullNumber + "\"]")) {
            System.out.println("Phone number is correct");
            // delete customer
            delete(driver);
        } else {
            System.out.println(FullNumber);
            System.out.println("Phone number is incorrect");
        }
        clearTheSearchedQuery();
    }
    public void delete(AppiumDriver driver) throws Throwable {
        tapOnElement("Booking.threeDotsEventButton", "Three dots in the overview page", driver);
        tapOnElement("Booking.deleteEventButton", "Delete button", driver);
        tapOnElement("Booking.deletecustomerName", "Got it button", driver);
        tapOnElement("Booking.finalConfirm", "Delete -confirmation popup button", driver);
    }
    public String getTextOfElement(AppiumDriver driver, String element) throws Throwable {
        String Locator = ReadProperties(element, LocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        return value.getText();

    }

    public void clickElementByXPath(String xpath) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
        } catch (WebDriverException e) {
            throw new Exception("Element not found or not clickable: " + xpath, e);
        }
    }

    public void sendKeysByXpath(String xpath, String value) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).sendKeys(value);
        } catch (WebDriverException e) {
            throw new Exception("Element not found: " + xpath, e);
        }
    }

    public boolean isDisplayed(String xpath) {
        try {
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void clearTheSearchedQuery() throws Exception {
//        clickElementByXPath(backBtn);
//        clickElementByXPath(clearSearchedQuery);
        Thread.sleep(1000);
        clickElementByXPath(cancelSearch);
        clickElementByXPath(calendarBtn);
    }
}
