package Tests;

import BaseClass.Capabilities;
import Utils.CustomerCreateFunctions;
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

public class iOSCustomerCreate extends CustomerCreateFunctions {
    IOSDriver driver;

    @BeforeClass
    public void setup() throws Throwable {
        driver = (IOSDriver) launchDriver("iOS");
    }

    @BeforeTest
    public void beforeTest() {
        beforeTesting();
    }

    public void testCustomerCreate(AppiumDriver driver, String countryName, String phoneNumber) throws Exception {
        createCustomer(driver, countryName, phoneNumber);
        openCustomerOverview(driver, countryName);
        validatePhoneNumber(driver, phoneNumber);
    }

}
