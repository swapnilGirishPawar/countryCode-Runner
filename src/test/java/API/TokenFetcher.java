package API;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.google.gson.Gson;

public class TokenFetcher {

    private static final String TOKEN_URL = "https://fullcreative.fullauth.com/o/oauth2/v1/token";
    private static final String CLIENT_ID = "7f001-a4656cce501ee97bdc09";
    private static final String CLIENT_SECRET = "ZE3Ao6ITX7v8mmzMYczPKjb1Q99cv2J5BMXfQvaq";
    private static final String USERNAME = "countrycode@maildrop.cc";
    private static final String PASSWORD = "setmore23";

    /**
     * Fetches token data and returns it as [access_token, user_id, acct_id].
     */
    public String[] getData() throws Exception {
        Map<String, String> tokenData = getToken();
        String accessToken = tokenData.get("access_token");
        String userId = tokenData.get("user_id");
        String accountId = tokenData.get("acct_id");

        return new String[]{accessToken, userId, accountId};
    }

    /**
     * Sends a token request and returns the parsed response as a Map.
     */
    private Map<String, String> getToken() throws Exception {
        String requestBody = buildRequestBody();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch token: HTTP " + response.statusCode() + " - " + response.body());
        }

        return parseResponse(response.body());
    }

    /**
     * Builds the request body for the token request.
     */
    private String buildRequestBody() {
        return "grant_type=password&" +
                "username=" + URLEncoder.encode(USERNAME, StandardCharsets.UTF_8) + "&" +
                "password=" + URLEncoder.encode(PASSWORD, StandardCharsets.UTF_8) + "&" +
                "client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8) + "&" +
                "client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8) + "&" +
                "scope=" + URLEncoder.encode(
                "setmore.apis-internal.fullaccess contacts-api.full_access awapis.fullaccess anywherecontacts.read "
                        + "fullhistory.read fullhistory.client.read anywherecontacts.write scheduling.events.read "
                        + "scheduling.events.manage scheduling.assets.read scheduling.assets.manage", StandardCharsets.UTF_8);
    }

    /**
     * Parses the JSON response string into a Map.
     */
    private Map<String, String> parseResponse(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, Map.class);
    }
}