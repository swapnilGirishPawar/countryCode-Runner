package API;

import org.json.JSONArray;
import org.json.JSONObject;

public class CountryService {
    private APIHandler apiHandler;
    private String acctId;

    /**
     * Constructor that initializes APIHandler and retrieves acct_id from TokenFetcher.
     */
    public CountryService() throws Exception {
        this.apiHandler = new APIHandler(); // Can throw exception if token fetching fails
        TokenFetcher tokenFetcher = new TokenFetcher();
        String[] tokenData = tokenFetcher.getData(); // Retrieve token data
        this.acctId = tokenData[2]; // acct_id is the third element in the tokenData array
    }

    /**
     * Returns an array with both the linkedLocation "id" and linkedContactMethods "contactID".
     * @return String[] where index 0 is linkedLocation "id" and index 1 is linkedContactMethods "contactID"
     */
    public String[] getLocationAndContactId() throws Exception {
        // Replace acct_id in the URL dynamically with the value from TokenFetcher
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
}
