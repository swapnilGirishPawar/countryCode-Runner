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
    public JSONArray readCountryDetails(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath)).useDelimiter("\\A");
        String jsonString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return new JSONArray(jsonString);
    }
}
