package Tests;

import BaseClass.Capabilities;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class AndroidCustomerCreate extends Capabilities {
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
    public void beforeTest() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("src/test/resources/AndroidLocators.properties")) {
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
    public String getTextOfElement(AppiumDriver driver, String element) throws Throwable {
        String Locator = ReadProperties(element, AndroidLocatorPropertiesFile);
        WebElement value = StringToElementConverter(Locator, driver);
        return value.getText();

    }
    public void validatePhoneNumber(AppiumDriver driver, String dialingCode, String phoneNumber) throws Throwable {
        String FullNumber = "+" +dialingCode + phoneNumber;
        String phoneNumberOverView = getTextOfElement(driver, "General.phoneNumberOverview").replaceAll("[\\s-]", "");
        if(phoneNumberOverView.equalsIgnoreCase(FullNumber)){
            System.out.println("Phone number is correct");
            // delete customer
            delete(driver);
            clearTheSearchedQuery();
        } else {
            System.out.println("Phone number is incorrect");
            System.out.println("wrong phone number on page - "+ phoneNumberOverView);
            System.out.println("expected phone number - "+ FullNumber);
            clickElementByXPath(backBtn);
            clickElementByXPath(clearSearchedQuery);
            clearTheSearchedQuery();
        }
    }
    public void openCustomerOverview(String customerName) throws Exception {
        clickElementByXPath(customerTab);
        sendKeysByXpath(customerSearchBar, customerName);
        Thread.sleep(2000);
        driver.findElement(By.id(customerName)).click();
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
            System.out.println("\uD83D\uDEA8 \uD83D\uDEA8 \uD83D\uDEA8 Customer created Failed!!");
            exitCustomerCreate();
            return countryName;
        } else {
            System.out.println("✅ ✅ ✅Customer created Successfully!!");
            gobackToCalendar();
        }
        return null;
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
    public void exitCustomerCreate() throws Exception {
        clickElementByXPath(cancelBtn);
        clickElementByXPath(discardNleave);
        clickElementByXPath(calendarBtn);
    }
    public void gobackToCalendar() throws Exception {
        clickElementByXPath(backBtn);
        clickElementByXPath(calendarBtn);
    }
    public void delete(AppiumDriver driver) throws Throwable {
        tapOnElementAndroid("Booking.threeDotsEventButton", "Three dots in the overview page", driver);
        tapOnElementAndroid("Booking.deleteEventButton", "Delete button", driver);
        tapOnElementAndroid("Booking.deletecustomerName", "Got it button", driver);
        tapOnElementAndroid("Booking.finalConfirm", "Delete -confirmation popup button", driver);
    }
    public void clearTheSearchedQuery() throws Exception {
//        clickElementByXPath(backBtn);
//        clickElementByXPath(clearSearchedQuery);
        Thread.sleep(1000);
        clickElementByXPath(cancelSearch);
        clickElementByXPath(calendarBtn);
    }
}
