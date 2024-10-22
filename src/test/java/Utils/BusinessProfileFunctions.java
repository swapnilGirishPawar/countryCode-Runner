package Utils;

import BaseClass.Capabilities;

import java.util.ArrayList;
import java.util.List;

public class BusinessProfileFunctions extends Capabilities {
    String CountryVal = "India";
    public List<String> SearchedCountries = new ArrayList<String>();

    public void NavigateToBusinessProfile() throws Throwable {
        System.out.println("Navigating to Business Profile");
        tapOnElement("iOSBottomNavBar.settings", "Settings");
        tapOnElement("iOSSettings.yourBrand", "Your Brand");
    }

    public void UpdateCountry() throws Throwable {
        tapOnElement("iOSYourBrand.editIcon", "Edit Icon");
        tapOnElement("iOSYourBrand.Country", "Country");
        tapOnElement("iOSYourBrand.countrySearchBar", "Search Bar");
        type("iOSYourBrand.countrySearchBar", CountryVal, "Search Bar");
        SearchedCountries = GetListOfSearchedCountries();
        tapOnElement("iOSYourBrand.Save", "Save Button");
    }

}
