package Utils;

import BaseClass.Capabilities;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class CustomerCreateFunctions extends Capabilities {
    String customerTab;
    String addNewCustomer;
    String addCustomerManually;
    String customerName;
    String phoneNumber;
    String saveButton;
    String customerSearchBar;
    String backBtn;
    String calendarBtn;

    public void beforeTesting(){
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("src/test/java/Utils/iOSLocators.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerTab = properties.getProperty("customerTab");
        addNewCustomer= properties.getProperty("addNewCustomer");
        addCustomerManually= properties.getProperty("addCustomerManually");
        customerName= properties.getProperty("customerName");
        phoneNumber= properties.getProperty("customerPhonenumber");
        saveButton= properties.getProperty("saveButton");
        customerSearchBar= properties.getProperty("customerSearchBar");
        backBtn= properties.getProperty("backBtn");
        calendarBtn= properties.getProperty("calendarBtn");

    }

    public void createCustomer(AppiumDriver driver,String countryName, String newPhoneNumber) throws Exception {
        beforeTesting();
        clickElementByXPath(driver, customerTab);
        clickElementByXPath(driver, addNewCustomer);
        clickElementByXPath(driver, addCustomerManually);
        sendKeysByXpath(driver, customerName,countryName);
        sendKeysByXpath(driver, phoneNumber, newPhoneNumber);
        clickElementByXPath(driver, saveButton);
        Thread.sleep(2000);

        if (isDisplayed(driver, saveButton)) {
            System.out.println("Customer created Failed!!");
        } else {
            System.out.println("Customer created Successfully!!");
        }
        gobackToCalendar(driver);
    }

    public void openCustomerOverview(AppiumDriver driver, String customerName) throws Exception {
        clickElementByXPath(driver, customerTab);
        sendKeysByXpath(driver, customerSearchBar, customerName);
        Thread.sleep(2000);
        driver.findElement(By.id(customerName)).click();
    }

    public void gobackToCalendar(AppiumDriver driver) throws Exception {
        clickElementByXPath(driver,backBtn);
        clickElementByXPath(driver, calendarBtn);
    }

    public void validatePhoneNumber(AppiumDriver driver, String phoneNumber) {
        if (isDisplayed(driver, "//XCUIElementTypeButton[@name=\"+91"+phoneNumber+"\"]")) {
            System.out.println("Phone number is correct");
        } else {
            System.out.println("Phone number is incorrect");
        }
    }

    public void clickElementByXPath(AppiumDriver driver, String xpath) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
        } catch (WebDriverException e) {
            throw new Exception("Element not found or not clickable: " + xpath, e);
        }
    }

    public void sendKeysByXpath(AppiumDriver driver, String xpath, String value) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).sendKeys(value);
        } catch (WebDriverException e) {
            throw new Exception("Element not found: " + xpath, e);
        }
    }

    public boolean isDisplayed(AppiumDriver driver, String xpath) {
        try {
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        }
        catch (WebDriverException e) {
            return false;
        }
    }
}
