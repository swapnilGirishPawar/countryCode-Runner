package Tests;

import BaseClass.Capabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
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
    int i=0;


    @BeforeClass
    public void setup() throws Throwable {
        driver = (IOSDriver) launchDriver("iOS");
    }

    @BeforeTest
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

    @Test
    public void testCustomerCreate() throws Exception {
        jsonValidation();
    }

    public void jsonValidation() throws Exception {
        while(i<=2){
            createCustomer();
            openCustomerOverview("Sudhansu1234");
            validatePhoneNumber("9090031459");
        }
        System.out.println("JSON validation completed");
    }

    public void createCustomer() throws Exception {
        clickElementByXPath(customerTab);
        clickElementByXPath(addNewCustomer);
        clickElementByXPath(addCustomerManually);
        sendKeysByXpath(customerName, "Sudhansu1234");
        sendKeysByXpath(phoneNumber, "909003145");
        clickElementByXPath(saveButton);
        Thread.sleep(2000);
        i++;
        System.out.println(i);

        if (isDisplayed(saveButton)) {
            System.out.println("Customer created Failed!!");
            exitCustomerCreate();
        } else {
            System.out.println("Customer created Successfully!!");
            gobackToCalendar();
        }

    }

    public void openCustomerOverview(String customerName) throws Exception {
        clickElementByXPath(customerTab);
        sendKeysByXpath(customerSearchBar, customerName);
        Thread.sleep(2000);
        driver.findElement(By.id("Sudhansu1234")).click();
    }

    public void exitCustomerCreate() throws Exception {
        clickElementByXPath(cancelBtn);
        clickElementByXPath(discardNleave);
        clickElementByXPath(calendarBtn);
        jsonValidation();
    }

    public void gobackToCalendar() throws Exception {
        clickElementByXPath(backBtn);
        clickElementByXPath(calendarBtn);
    }

    public void validatePhoneNumber(String phoneNumber) throws Exception {
        if (isDisplayed("//XCUIElementTypeButton[@name=\"+91" + phoneNumber + "\"]")) {
            System.out.println("Phone number is correct");
        } else {
            System.out.println("Phone number is incorrect");
        }
        clearTheSearchedQuery();
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
        clickElementByXPath(backBtn);
        clickElementByXPath(clearSearchedQuery);
        clickElementByXPath(cancelSearch);
        clickElementByXPath(calendarBtn);
    }
}
