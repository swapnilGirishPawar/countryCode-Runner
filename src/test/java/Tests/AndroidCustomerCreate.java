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
    String searchboxType;
    String phoneNumber;
    String saveButton;
    String customerSearchBar;
    String backBtn;
    String calendarBtn;
    String cancelBtn;
    String discardNleave;
    String clearSearchedQuery;
    String cancelSearch;
    String customerOverview3Dot;
    String customerDelete;
    String customerYesDelete;
    String customerDeleteConfirm;
    String customerOverviewPhonenumber;
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
        searchboxType = properties.getProperty("searchboxType");
        customerOverview3Dot = properties.getProperty("customerOverview3Dot");
        customerDelete = properties.getProperty("customerDelete");
        customerYesDelete = properties.getProperty("customerYesDelete");
        customerDeleteConfirm = properties.getProperty("customerDeleteConfirm");
        customerOverviewPhonenumber = properties.getProperty("customerOverviewPhonenumber");

    }
    public String customerFlow(String countryName, String phoneNumber, String dialingCode, AppiumDriver driver) throws Throwable {
        beforeTest();
        String FailedCountry = createCustomer(countryName, phoneNumber,dialingCode);
        if(FailedCountry == null){
            //openCustomerOverview(countryName);
            //validatePhoneNumber(driver, dialingCode, phoneNumber);
        }
        return FailedCountry;

    }
    public String getTextOfElement(AppiumDriver driver, String element) throws Throwable {
        return driver.findElement(By.xpath(element)).getText();
    }
    public void validatePhoneNumber(AppiumDriver driver, String dialingCode, String phoneNumber) throws Throwable {
        String FullNumber = "+" + dialingCode + phoneNumber;
        String phoneNumberOverView = getTextOfElement(driver, customerOverviewPhonenumber).replaceAll("\\s", "");
        if (phoneNumberOverView.equalsIgnoreCase(FullNumber)) {
//        if (isDisplayed("//XCUIElementTypeButton[@name=\"" + FullNumber + "\"]")) {
            System.out.println("Phone number is correct");
            node.pass("Phone number is correct");
            // delete customer
            delete();
        } else {
            System.out.println(FullNumber);
            System.out.println("Phone number is incorrect");
        }
        clearTheSearchedQuery();
    }
    public void openCustomerOverview(String customerName) throws Exception {
        clickElementByXPath(customerTab);
        clickElementByXPath(customerSearchBar);
        sendKeysByXpath(searchboxType, customerName);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + customerName + "\"]")).click();
    }
    public String createCustomer(String countryName, String mobileNumber, String dialingCode) throws Exception {
        clickElementByXPath(customerTab);
        clickElementByXPath(addNewCustomer);
        clickElementByXPath(addCustomerManually);
        sendKeysByXpath(customerName, countryName);
        node.info("Customer name: " + countryName);
        sendKeysByXpath(phoneNumber, mobileNumber);
        node.info("Creating customer with phone number:  " +"+"+ dialingCode+" " + mobileNumber);
        clickElementByXPath(saveButton);
        node.info("Clicked on save button");
        try {
            wait(By.xpath(backBtn), driver, 20);
            clickElementByXPath(backBtn);
            System.out.println("✅ ✅ ✅Customer created Successfully!!");
            node.pass("✅ ✅ ✅Customer created successfully");
        } catch (Exception e) {
            if (isDisplayed(saveButton)) {
                System.out.println("❌ ❌ ❌Customer created Failed!!");
                node.fail("❌ ❌ ❌Customer created Failed!!");
                exitCustomerCreate();
                return countryName;
            }
        }

//        if (isDisplayed(saveButton)) {
//            System.out.println("\uD83D\uDEA8 \uD83D\uDEA8 \uD83D\uDEA8 Customer created Failed!!");
//            exitCustomerCreate();
//            return countryName;
//        } else {
//            System.out.println("✅ ✅ ✅Customer created Successfully!!");
//            gobackToCalendar();
//        }
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
    public void delete() throws Throwable {
        clickElementByXPath(customerOverview3Dot);
        clickElementByXPath(customerDelete);
        clickElementByXPath(customerYesDelete);
        clickElementByXPath(customerDeleteConfirm);
    }
    public void clearTheSearchedQuery() throws Exception {
        Thread.sleep(1000);
        clickElementByXPath(calendarBtn);
    }

}
