package API;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountryUpdateService {
    private APIHandler apiHandler;
    private String[] contactInfo; // Stores contactId and locationId

    public CountryUpdateService() throws Exception {
        this.apiHandler = new APIHandler(); // Initialize APIHandler
        CountryService countryService = new CountryService(); // Get contact info from CountryService
        this.contactInfo = countryService.getLocationAndContactId(); // [locationId, contactId]
    }

    /**
     * Reads the JSON file containing country details.
     * @param filePath The path to the JSON file.
     * @return A JSONArray of country details.
     */
    private JSONArray readCountryDetails(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath)).useDelimiter("\\A");
        String jsonString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return new JSONArray(jsonString);
    }

    /**
     * Loops through the countries and updates each using the API.
     * @param countryDetails JSONArray containing country details.
     */
    public void updateCountries(JSONArray countryDetails) {
        String locationId = contactInfo[0]; // Location ID from getLocationAndContactId()
        String contactId = contactInfo[1];  // Contact ID from getLocationAndContactId()

        for (int i = 0; i < countryDetails.length(); i++) {
            JSONObject country = countryDetails.getJSONObject(i);
            String name = country.getString("name");
            String phoneNumber = country.optString("phoneNumber", ""); // Extract but not used in the request body

            // Store phoneNumber for future use (if needed)
            System.out.println("Phone number for future use: " + phoneNumber);

            // Prepare the URL with the dynamic contactId
            String url = "https://contacts.anywhere.co/services/data/v2.0/objects/Contact/"
                    + contactId + "/businessProfile";

            // Prepare the request body without the phone number
            String requestBody = String.format(
                    """
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
    }

    public static void main(String[] args) {
        try {
            // Initialize CountryUpdateService
            CountryUpdateService updateService = new CountryUpdateService();

            // Read the JSON file with country details
            String filePath = "/path/to/countryDetails.json"; // Replace with your JSON file path
            JSONArray countryDetails = updateService.readCountryDetails(filePath);

            // Update countries using the API
            updateService.updateCountries(countryDetails);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
