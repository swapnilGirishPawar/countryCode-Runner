package API;

import lombok.Getter;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIHandler {

        private String accessToken;

        //Constructor that initializes the access token by fetching it using TokenFetcher.
        public APIHandler() throws Exception {
            TokenFetcher tokenFetcher = new TokenFetcher();
            String[] tokenData = tokenFetcher.getData();
            this.accessToken = tokenData[0];
        }

        public String getAccessToken() {
            return accessToken;
        }
        /**
         * Reusable API request method with optional token refresh logic.
         *
         * @param requestMethod The HTTP method (e.g., "GET", "POST").
         * @param urlString The URL for the API request.
         * @param requestBody The request body (if applicable, otherwise null).
         * @return The API response as a string.
         * @throws Exception If an error occurs during the request.
         */
        public String makeAPIRequest(String requestMethod, String urlString, String requestBody) throws Exception {
            HttpURLConnection connection = createConnection(requestMethod, urlString, requestBody);
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                return readResponse(connection);
            } else if (responseCode == 401) {
                // Refresh the token and retry the request
                TokenFetcher tokenFetcher = new TokenFetcher();
                String[] tokenData = tokenFetcher.getData(); // Retrieve a new token
                this.accessToken = tokenData[0]; // Update the access token
                connection.disconnect();
                return makeAPIRequest(requestMethod, urlString, requestBody); // Retry the request
            } else {
                throw new RuntimeException("API request failed with response code: " + responseCode);
            }
        }

        /**
         * Creates an HTTP connection for the API request.
         *
         * @param method The HTTP method to use (e.g., "GET", "POST", "DELETE").
         * @param urlString The URL for the request.
         * @param body The request body (optional).
         * @return The HttpURLConnection object.
         * @throws Exception If an error occurs during connection setup.
         */
        private HttpURLConnection createConnection(String method, String urlString, String body) throws Exception {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            if (body != null && !body.isEmpty()) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            return connection;
        }

        /**
         * Reads the response from the API as a string.
         *
         * @param connection The HttpURLConnection object.
         * @return The response as a string.
         * @throws IOException If an error occurs while reading the response.
         */
        private String readResponse(HttpURLConnection connection) throws IOException {
            try (Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A")) {
                return scanner.hasNext() ? scanner.next() : "";
            }
        }
}
