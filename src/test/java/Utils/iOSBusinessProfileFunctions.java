package Utils;

import API.APIHandler;
import API.CountryServiceManager;
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
    String filePath = "src/test/resources/testphone.json";

    public void NavigateToCustomersTab(AppiumDriver driver) throws Throwable {
        tapOnElementiOS("iOSBottomNavBar.Customers", "Customers", driver);
    }

    public void UpdateCountryAPIRequest(String contactId, String phoneNumber, String name, String locationId) {
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
        // Initialize CountryServiceManager to retrieve contact information and read country details
        CountryServiceManager serviceManager = new CountryServiceManager();
        this.apiHandler = new APIHandler();

        // Get locationId and contactId using CountryServiceManager
        this.contactInfo = serviceManager.getContactInfo();
        String locationId = contactInfo[0];
        String contactId = contactInfo[1];

        // Read country details from the JSON file
        JSONArray countries = serviceManager.readCountryDetails(filePath);

        String PhoneNumber = null;
        CustomerService customerService = new CustomerService();

        for (int i = 0; i < countries.length(); i++) {
            JSONObject countryObj = countries.getJSONObject(i);
            String countryName = countryObj.getString("name");
            String dialingCode = countryObj.getString("dialingCode");
            node = test.createNode("Country: " + countryName);
            try {
                PhoneNumber = countryObj.getString("phoneNumber");

            } catch (NullPointerException e) {
                failedCountries.add(countryName);
                System.out.println("⚠️ Country skipped due to unavailable phone number in JSON file: " + countryName);
                System.out.println("________________________________________________________________________________________");
                continue;
            }
            // Update the country API request with contact and location information
            UpdateCountryAPIRequest(contactId, PhoneNumber, countryName, locationId);

            // Perform customer flow and create customer using the iOSCustomerCreate class
            obj.customerFlow(countryName, PhoneNumber, dialingCode, driver);

            // Fetch and delete the customer record
            String customerId = customerService.fetchAndCheckCustomer(accountId, countryName, PhoneNumber, dialingCode);
            customerService.deleteCustomer(customerId, accountId);

            System.out.println("________________________________________________________________________________________");
        }

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }
}