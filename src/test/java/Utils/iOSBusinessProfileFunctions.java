package Utils;

import API.APIHandler;
import API.CountryService;
import API.CountryUpdateService;
import API.CustomerService;
import BaseClass.Capabilities;
import Tests.iOSCustomerCreate;
import io.appium.java_client.AppiumDriver;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class iOSBusinessProfileFunctions extends Capabilities {
    public static String selectedCountry;
    List<String> PassedCountries = new ArrayList<>();
    List<String> failedCountries = new ArrayList<>();
    iOSCustomerCreate obj = new iOSCustomerCreate();
    private APIHandler apiHandler;
    private String[] contactInfo;
    String filePath = "src/test/resources/testphone.json"; // Replace with your JSON file path

    public void NavigateToCustomersTab(AppiumDriver driver) throws Throwable {
        tapOnElementiOS("iOSBottomNavBar.Customers", "Customers", driver);
    }

    public void UpdateCountry(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        tapOnElementiOS("iOSYourBrand.editIcon", "Edit Icon", driver);
        tapOnElementiOS("iOSYourBrand.Country", "Country", driver);
        tapOnElementiOS("iOSYourBrand.countrySearchBar", "Search Bar", driver);
        typeiOS("iOSYourBrand.countrySearchBar", countryName, "Search Bar", driver);
        Thread.sleep(2000);
        selectedCountry = SelectProperCountryiOS(countryName, driver);
        if (!validateString(countryName, selectedCountry)) {
            failedCountries.add(countryName);
        } else {
            System.out.println("✅ Selected country matched with country value from JSON");
            PassedCountries.add(countryName);

        }
        tapOnElementiOS("iOSYourBrand.Save", "Save Button", driver);
    }

    public void countryCodeChnger(AppiumDriver driver, String countryName, String phoneNumber) throws Throwable {
        UpdateCountry(driver, countryName, phoneNumber);
    }

    public void navigateFromBusinessProfileToCustomerTab(AppiumDriver driver) throws Throwable {
        Thread.sleep(6000);
        tapOnElement2("accessibilityid$anywhere_back", "your brand back button", driver);
        tapOnElementiOS("iOSBottomNavBar.Customers", "Customers tab", driver);
    }

    public void UpdateCountryAPIRequest(String contactId, String phoneNumber, String name, String locationId) {
// Store phoneNumber for future use (if needed)
        System.out.println("Phone number for future use: " + phoneNumber);

        // Prepare the URL with the dynamic contactId
        String url = "https://contacts.anywhere.co/services/data/v2.0/objects/Contact/" + contactId + "/businessProfile";

        // Prepare the request body without the phone number
        String requestBody = String.format("""
                {
                    "linkedLocations": [
                        {
                            "country": "%s",
                            "id": "%s"
                        }
                    ]
                }
                """, name, locationId);
        try {
            // Make the API request
            String response = apiHandler.makeAPIRequest("PUT", url, requestBody);

            // Check if the response indicates success
            if (response != null) {
                System.out.println("Country value is updated to " + name);
            }
        } catch (Exception e) {
            System.err.println("Country value is not updated: " + name);
            e.printStackTrace();
        }
    }

    public void iOSLooper(AppiumDriver driver) throws Throwable {
        CountryUpdateService updateService = new CountryUpdateService();
        this.apiHandler = new APIHandler();
        CountryService countryService = new CountryService();
        this.contactInfo = countryService.getLocationAndContactId();
        JSONArray countries = updateService.readCountryDetails(filePath);
        String locationId = contactInfo[0];
        String contactId = contactInfo[1];
        String PhoneNumber = null;
        CustomerService customerService = new CustomerService();

        for (int i = 0; i < countries.length(); i++) {
            JSONObject countryObj = countries.getJSONObject(i);
            String countryName = countryObj.getString("name");
            String dialingCode = countryObj.getString("dialingCode");
            try {
                PhoneNumber = countryObj.getString("phoneNumber");

            } catch (NullPointerException e) {
                failedCountries.add(countryName);
                System.out.println("⚠\uFE0F Country skipped due to unavailable phone number in JSON file " + countryName);
                System.out.println("________________________________________________________________________________________");
                continue;
            }

            // Update the value in business Profile
            UpdateCountryAPIRequest(contactId, PhoneNumber, countryName, locationId);

            // customer creation flow
            String FailedCountry = obj.customerFlow(countryName, PhoneNumber, dialingCode, driver);

            String customerId = customerService.fetchAndCheckCustomer(accountId, countryName, PhoneNumber, dialingCode);
            System.out.println("Customer ID for delete: " + customerId);
            System.out.println("account ID for delete: " + accountId);

            customerService.deleteCustomer(customerId, accountId);

            if (FailedCountry != null) {
                failedCountries.add(FailedCountry);
            }
            // navigate back to customer tab
            NavigateToCustomersTab(driver);

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
