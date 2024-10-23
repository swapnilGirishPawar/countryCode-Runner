package Utils;

import BaseClass.Capabilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.AppiumDriver;

import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class BusinessProfileFunctions extends Capabilities {
    public static String selectedCountry;
    List<String> PassedCountries = new ArrayList<>();
    List<String> failedCountries = new ArrayList<>();

    public void NavigateToBusinessProfile(AppiumDriver driver) throws Throwable {
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

        }
        else {
            System.out.println("Selected country matched with country value from JSON");
        }
        tapOnElement("iOSYourBrand.Save", "Save Button", driver);
    }

    public void countryCodeChnger(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        UpdateCountry(driver,countryName, phoneNumber);
    }

    public void navigateFromBusinessProfileToCustomerTab(AppiumDriver driver) throws Throwable {
        System.out.println("Navigating from Business Profile to Customer Creation");
        tapOnElement2("xpath$//XCUIElementTypeButton[@name=\"anywhere_back\"]", "your brand back button", driver);
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
            System.out.println("Country under test - " + countryName);

            // Update the value in business Profile
            countryCodeChnger(driver, countryName, PhoneNumber);

            // navigate back to customer tab.

            if (!validateCustomerCountry(countryName)) failedCountries.add(countryName);
            else PassedCountries.add(countryName);

            // customer creation flow

            // navigate back to business Profile

        }

        if (failedCountries.isEmpty()) {
            System.out.println("All countries matched successfully!");
        } else {
            System.out.println("Countries that failed to match: " + failedCountries);
        }
    }

    public JsonArray readCountriesFromJson() throws Exception {
        FileReader reader = new FileReader("./src/test/resources/testphone.json");
        return JsonParser.parseReader(reader).getAsJsonArray();
    }

}
