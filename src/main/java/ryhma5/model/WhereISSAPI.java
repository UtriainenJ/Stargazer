package ryhma5.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WhereISSAPI{

    // id for the ISS - the only id the API supports
    private static final String id = Integer.toString(25544);
    public static ISSInfo fetchISS(String units, Long timestamp) throws Exception {
        // Construct the API URL
        StringBuilder apiUrl = new StringBuilder("https://api.wheretheiss.at/v1/satellites/" + id);

        // Add query parameters if provided
        if (units != null || timestamp != null) {
            apiUrl.append("?");
            if (units != null) {
                apiUrl.append("units=").append(URLEncoder.encode(units, "UTF-8"));
            }
            if (timestamp != null) {
                if (units != null) {
                    apiUrl.append("&");
                }
                apiUrl.append("timestamp=").append(timestamp);
            }
        }

        // Open the connection
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl.toString()).openConnection();
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // Success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response into an ISS object
            return parseISS(response.toString());
        } else {
            String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }
    private static ISSInfo parseISS(String jsonResponse) {
        // Use Gson to parse the JSON response
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, ISSInfo.class);
    }
}