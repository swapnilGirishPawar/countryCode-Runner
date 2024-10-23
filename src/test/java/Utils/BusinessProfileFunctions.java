package Utils;

import BaseClass.Capabilities;
import Tests.iOSCustomerCreate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import java.time.Duration;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BusinessProfileFunctions extends Capabilities {
    public String CountryName = "India";
    iOSCustomerCreate customerCreate = new iOSCustomerCreate();
    public static String selectedCountry;

    public void NavigateToBusinessProfile(AppiumDriver driver) throws Throwable {
        System.out.println("Navigating to Business Profile");
        tapOnElement("iOSBottomNavBar.settings", "Settings", driver);
        tapOnElement("iOSSettings.yourBrand", "Your Brand", driver);
    }

    public void UpdateCountry(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        tapOnElement("iOSYourBrand.editIcon", "Edit Icon", driver);
        tapOnElement("iOSYourBrand.Country", "Country", driver);
        tapOnElement("iOSYourBrand.countrySearchBar", "Search Bar", driver);
        type("iOSYourBrand.countrySearchBar", countryName, "Search Bar", driver);
        Thread.sleep(2000);
        selectedCountry = SelectProperCountry(countryName, driver);
        if(!validateString(countryName, selectedCountry)){
            System.out.println("Country Name from JSON and Selected Country is not matching");
        }
        tapOnElement("iOSYourBrand.Save", "Save Button", driver);
    }

    public void countryCodeChnger(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        UpdateCountry(driver,countryName, phoneNumber);
    }

    public void navigateFromBusinessProfileToCustomerTab(AppiumDriver driver) throws Throwable {
        System.out.println("Navigating from Business Profile to Customer Creation");
//        tapOnElement("common.backButton1", "your brand back button", driver);
        tapOnElement("iOSBottomNavBar.Customers", "Customers tab", driver);
    }


    public void looper(AppiumDriver driver) throws Throwable {
        JsonArray countries = readCountriesFromJson();
        NavigateToBusinessProfile(driver);

        for (int i = 0; i < countries.size(); i++) {
            // get value from JSON
            JsonObject countryObj = countries.get(i).getAsJsonObject();
            String countryName = countryObj.get("name").getAsString();
            String PhoneNumber = countryObj.get("phoneNumber").getAsString();
            System.out.println("Country Name: " + countryName);
            System.out.println("Phone Number: " + PhoneNumber);

            // Update the value in business Profile
            countryCodeChnger(driver, countryName, PhoneNumber);
            // navigate back to customer tab.
            navigateFromBusinessProfileToCustomerTab(driver);

            // customer creation flow
            customerCreate.testCustomerCreate(driver, countryName, PhoneNumber);

            // navigate back to business Profile
            NavigateToBusinessProfile(driver);

        }
    }

    public JsonArray readCountriesFromJson() throws Exception {
        FileReader reader = new FileReader("./src/test/resources/testphone.json");
        return JsonParser.parseReader(reader).getAsJsonArray();
    }

}
