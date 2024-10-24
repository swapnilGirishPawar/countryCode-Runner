package API;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIHandler {
    private TokenFetcher tokenFetcher;
    private String[] tokenData;
    private String accessToken;

    /**
     * Constructor that initializes the token data by calling getData().
     */
    public APIHandler() throws Exception {
        this.tokenFetcher = new TokenFetcher();
        this.tokenData = tokenFetcher.getData(); // This can throw an exception
        this.accessToken = tokenData[0];
    }

    /**
     * Reusable API request method with token refresh logic.
     */
    public String makeAPIRequest(String requestMethod, String urlString, String requestBody) throws Exception {
        HttpURLConnection connection = createConnection(requestMethod, urlString, requestBody);
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            return readResponse(connection);
        } else if (responseCode == 401) {
            // Refresh the token and retry
            this.tokenData = tokenFetcher.getData();
            this.accessToken = tokenData[0];
            connection.disconnect();
            return makeAPIRequest(requestMethod, urlString, requestBody); // Retry the request
        } else {
            throw new RuntimeException("API request failed with response code: " + responseCode);
        }
    }

    /**
     * Creates an HTTP connection for the API request.
     */
    private HttpURLConnection createConnection(String method, String urlString, String body) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        if (body != null) {
            connection.getOutputStream().write(body.getBytes());
        }

        return connection;
    }

    /**
     * Reads the response from the API as a string.
     */
    private String readResponse(HttpURLConnection connection) throws IOException {
        Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
