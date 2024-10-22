package Utils;

import BaseClass.Capabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

import java.util.ArrayList;
import java.util.List;

public class BusinessProfileFunctions extends Capabilities {
    String CountryVal = "United";
    public List<String> SearchedCountries = new ArrayList<String>();

    public void NavigateToBusinessProfile(AppiumDriver driver) throws Throwable {
        System.out.println("Navigating to Business Profile");
        tapOnElement("iOSBottomNavBar.settings", "Settings", driver);
        tapOnElement("iOSSettings.yourBrand", "Your Brand", driver);
    }

    public void UpdateCountry(AppiumDriver driver) throws Throwable {
        tapOnElement("iOSYourBrand.editIcon", "Edit Icon", driver);
        tapOnElement("iOSYourBrand.Country", "Country", driver);
        tapOnElement("iOSYourBrand.countrySearchBar", "Search Bar", driver);
        type("iOSYourBrand.countrySearchBar", CountryVal, "Search Bar", driver);
        Thread.sleep(2000);

//        SearchedCountries = GetListOfSearchedCountries(driver);
        tapOnElement("iOSYourBrand.Save", "Save Button", driver);
    }

}
