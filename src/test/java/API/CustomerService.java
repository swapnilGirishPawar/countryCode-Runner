package API;

import BaseClass.Capabilities;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CustomerService extends Capabilities {
    private APIHandler apiHandler;

    public CustomerService() throws Exception {
        this.apiHandler = new APIHandler(); // Initialize APIHandler
    }

    /**
     * Deletes a customer based on the given customerId and accountId.
     *
     * @param customerId The ID of the customer to delete.
     * @param accountId  The account ID to use in the request header.
     */
    public void deleteCustomer(String customerId, String accountId) {
        try {
            // Construct the DELETE request URL with the customerId
            String urlString = "https://setmore.contacts.setmore.com/api/v3.0/resources/Customer/" + customerId;

            // Prepare headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + apiHandler.getAccessToken());
            headers.put("account-id", accountId);
            headers.put("Content-Type", "application/json");

            System.out.println("Attempting to delete customer...");
            System.out.println("URL: " + urlString);
            System.out.println("Headers: " + headers);

            // Make the DELETE request
            int responseCode = makeDeleteRequest(urlString, headers);

            // Handle the response
            if (responseCode == 200) {
                System.out.println("Customer deleted successfully.");
                node.pass("Customer deleted successfully.");
            } else {
                System.err.println("Failed to delete customer. Response code: " + responseCode);
                node.fail("Failed to delete customer. Response code: " + responseCode);
            }

        } catch (IOException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Makes a DELETE request with the given URL and headers.
     *
     * @param urlString The URL for the DELETE request.
     * @param headers   A map of header key-value pairs.
     * @return The response code from the DELETE request.
     * @throws IOException If an I/O error occurs.
     */
    private int makeDeleteRequest(String urlString, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("DELETE");

        // Set headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        // Send the request and return the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Message: " + connection.getResponseMessage());
        connection.disconnect(); // Close the connection
        return responseCode;
    }

    /**
     * Fetches the customer list using the provided token data and checks if the given
     * name and phone number are present. If found, returns the contact ID; otherwise, returns null.
     *
     * @param accountId   The account ID to use.
     * @param name        The full name to search for.
     * @param phoneNumber The phone number to search for.
     * @param dialingCode The dialing code for the phone number.
     * @return The ID of the matching contact, or null if not found.
     */
    public String fetchAndCheckCustomer(String accountId, String name, String phoneNumber, String dialingCode) {
        try {
            // Construct the URL for fetching customer data
            String url = String.format(
                    "https://sync.contacts.anywhere.co/services/data/v2.0/objects/Contact/contactSync?apikey=%s&category=person&deleted=false&limit=30&orderBy=DESC",
                    accountId
            );

            // Make the GET request using APIHandler
            String customerListResponse = apiHandler.makeAPIRequest("GET", url, null);

            // Parse the JSON response
            JSONObject responseJson = new JSONObject(customerListResponse);
            System.out.println("Response of fetch response: " + responseJson);

            // Check if the response contains contacts
            if (responseJson.getBoolean("success") && responseJson.has("contact")) {
                JSONArray contacts = responseJson.getJSONArray("contact");

                // Loop through contacts to find a match
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject contact = contacts.getJSONObject(i);

                    // Check if the full name matches
                    if (name.contains(contact.optString("firstName"))) {
                        JSONArray linkedContactMethods = contact.optJSONArray("linkedContactMethods");

                        // Check if linked contact methods contain the phone number
                        if (linkedContactMethods != null) {
                            for (int j = 0; j < linkedContactMethods.length(); j++) {
                                JSONObject method = linkedContactMethods.getJSONObject(j);

                                // Check if the phone number matches
                                String formattedPhone = "+" + dialingCode + phoneNumber;
                                if (method.optString("value").equals(formattedPhone)) {
                                    System.out.println("Matching customer found.");
                                    String id = method.getString("contactID");
                                    System.out.println(name + " and " + phoneNumber + " are saved successfully in the DB.");
                                    node.pass("Customer name and phone number are matched successfully in the DB.");
                                    return id; // Return the matching customer ID
                                }
                            }
                        }
                    }
                }
            }

            // If no match found, print a message and return null
            System.out.println(name + " and " + phoneNumber + " are not saved in the DB.");
            node.fail("Customer name and phone number are not matched in the DB.");
            return null;

        } catch (Exception e) {
            System.err.println("Error fetching or checking customer: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}