package API;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountryServiceManager {
    private APIHandler apiHandler;
    private String acctId;
    private String[] contactInfo; // Stores locationId and contactId

    /**
     * Constructor that initializes APIHandler, retrieves acct_id from TokenFetcher,
     * and retrieves location and contact information from the API.
     */
    public CountryServiceManager() throws Exception {
        this.apiHandler = new APIHandler();

        // Fetch acct_id from TokenFetcher
        TokenFetcher tokenFetcher = new TokenFetcher();
        String[] tokenData = tokenFetcher.getData();
        this.acctId = tokenData[2];

        // Fetch location and contact information
        this.contactInfo = getLocationAndContactId(); // [locationId, contactId]
    }

    /**
     * Returns an array with both the linkedLocation "id" and linkedContactMethods "contactID".
     * @return String[] where index 0 is linkedLocation "id" and index 1 is linkedContactMethods "contactID"
     */
    public String[] getLocationAndContactId() throws Exception {
        String url = "https://contacts.anywhere.co/services/data/v2.0/objects/Account/" + acctId + "/getBusinessProfile";

        String requestBody = """
            {
              "fieldInfoIds": [
                "904b0309-0435-4df3-a3d3-ae90707b6573",
                "f191a45f-63b6-4779-a147-32b55694a1c7",
                "a7817b12-d247-42b2-8f86-fb51fd5ec614"
              ]
            }
        """;

        String response = apiHandler.makeAPIRequest("POST", url, requestBody);
        JSONObject jsonResponse = new JSONObject(response);

        // Extract the first linkedLocation "id"
        JSONArray linkedLocations = jsonResponse.getJSONObject("company").getJSONArray("linkedLocations");
        String locationId = linkedLocations.getJSONObject(0).getString("id");

        // Extract the first linkedContactMethods "contactID"
        JSONArray linkedContactMethods = jsonResponse.getJSONObject("company").getJSONArray("linkedContactMethods");
        String contactId = linkedContactMethods.getJSONObject(0).getString("contactID");

        // Return both values as a String array
        return new String[]{locationId, contactId};
    }



    /**
     * Reads a JSON array from a file at the given path.
     * @param filePath The path to the file containing JSON data.
     * @return JSONArray containing the parsed JSON data.
     * @throws FileNotFoundException if the file does not exist.
     */
    public JSONArray readCountryDetails(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath)).useDelimiter("\\A");
        String jsonString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return new JSONArray(jsonString);
    }

    /**
     * Returns the contact information (locationId and contactId).
     * @return String[] where index 0 is locationId and index 1 is contactId
     */
    public String[] getContactInfo() {
        return contactInfo;
    }
}