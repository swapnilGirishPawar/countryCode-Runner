package Utils;

import BaseClass.Capabilities;
import Tests.AndroidCustomerCreate;
import Tests.iOSCustomerCreate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidBusinessProfileFunctions extends Capabilities {
    public static String selectedCountry;
    List<String> PassedCountries = new ArrayList<>();
    List<String> failedCountries = new ArrayList<>();
    AndroidCustomerCreate customer = new AndroidCustomerCreate();

    public void NavigateToBusinessProfile(AppiumDriver driver) throws Throwable {
        tapOnElementAndroid("BottomNavBar.settings", "Settings", driver);
        tapOnElementAndroid("settings.yourBrand", "Your Brand", driver);
    }
    public void countryCodeChnger(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        UpdateCountry(driver,countryName, phoneNumber);
    }
    public void UpdateCountry(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        tapOnElementAndroid("customers.editCustomers", "Edit Icon", driver);
        tapOnElementAndroid("customers.country", "Country", driver);
        tapOnElementAndroid("YourBrand.countrySearchBar", "Search Bar", driver);
        typeAndroid("YourBrand.countrySearchBarInput", countryName, "Search Bar", driver);
        Thread.sleep(2000);
        selectedCountry = SelectProperCountryAndroid(countryName, driver);
        if(!validateString(countryName, selectedCountry)){
            failedCountries.add(countryName);
        }
        else {
            System.out.println("✅ Selected country matched with country value from JSON");
            PassedCountries.add(countryName);

        }
        tapOnElementAndroid("YourBrand.saveButton", "Save Button", driver);
    }
    public void navigateFromBusinessProfileToCustomerTab(AppiumDriver driver) throws Throwable {
        Thread.sleep(6000);
        tapOnElementAndroid("YourBrand.backButton", "your brand back button", driver);
        tapOnElementAndroid("BottomNavBar.customers", "Customers tab", driver);
    }
    public void AndroidLooper(AppiumDriver driver) throws Throwable {
        System.out.println("Hello from AndroidLooper");
        JsonArray countries = readCountriesFromJson();
        NavigateToBusinessProfile(driver);
        String PhoneNumber = null;
        for (int i = 0; i < countries.size(); i++) {
            JsonObject countryObj = countries.get(i).getAsJsonObject();
            String countryName = countryObj.get("name").getAsString();;
            String dialingCode = countryObj.get("dialingCode").getAsString();;
            try {
                PhoneNumber = countryObj.get("phoneNumber").getAsString();

            } catch (NullPointerException e){
                failedCountries.add(countryName);
                System.out.println("⚠\uFE0F Country skipped due to unavailable phone number in JSON file " + countryName);
                System.out.println("________________________________________________________________________________________");
                continue;
            }
//          Update the value in business Profile
            countryCodeChnger(driver, countryName, PhoneNumber);

            // navigate back to customer tab.
            navigateFromBusinessProfileToCustomerTab(driver);
            // customer creation flow
//            String FailedCountry = obj.customerFlow(countryName, PhoneNumber,dialingCode, driver);

//            if(FailedCountry!=null){
//                failedCountries.add(FailedCountry);
//            }

            // navigate back to business Profile
            NavigateToBusinessProfile(driver);

            System.out.println("________________________________________________________________________________________");

        }

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        if (failedCountries.isEmpty()) {
            System.out.println("✅ ✅ ✅ All countries matched successfully! ✅ ✅ ✅");
        } else {
            System.out.println("\uD83D\uDEA8 \uD83D\uDEA8 \uD83D\uDEA8 Countries that failed to match: \n" + failedCountries);
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }


}
