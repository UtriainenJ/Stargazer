package ryhma5.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import com.google.gson.Gson;

public class AstronomyAPI {

    public static AstronomyEvent fetchAstronomyEvent(String body,
                                                     String latitude,
                                                     String longitude,
                                                     String elevation,
                                                     String fromDate,
                                                     String toDate,
                                                     String time) throws Exception {

        String applicationId = "a8b19fa3-5fd9-4285-994f-5704eb84baf5";
        String applicationSecret = "593aea4dedd5e83e7f58605ab827e97fb2ddc9325769915eec568a2e31aabf39d602bc87216783f49ec5d10f1e2877f9343106d473c661d0e76d75ff673587e960e3f7aa6c4870662f4a600cc28abbe0026813a4dd170c84a7820aeb543b7f5477641fd74436302c01dd72628c0089cc";

        // Construct the API URL without parameters
        String apiUrl = String.format("https://api.astronomyapi.com/api/v2/bodies/events/%s", body);

        // Base64 encode the applicationId:applicationSecret for Basic Auth
        String auth = applicationId + ":" + applicationSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // Set up the connection
        URL url = new URL(apiUrl + String.format(
                "?latitude=%s&longitude=%s&elevation=%s&from_date=%s&to_date=%s&time=%s",
                URLEncoder.encode(latitude, "UTF-8"),
                URLEncoder.encode(longitude, "UTF-8"),
                URLEncoder.encode(elevation, "UTF-8"),
                URLEncoder.encode(fromDate, "UTF-8"),
                URLEncoder.encode(toDate, "UTF-8"),
                URLEncoder.encode(time, "UTF-8")));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            return parseAstronomyEvent(response.toString());
        } else {
            String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    private static AstronomyEvent parseAstronomyEvent(String jsonResponse) {

        System.out.println("API Response: " + jsonResponse);

        Gson gson = new Gson();

        // Parse the entire JSON response into an AstronomyEvent object
        AstronomyEvent response = gson.fromJson(jsonResponse, AstronomyEvent.class);

        // Check if response and data are not null
        if (response != null && response.getData() != null) {
            // Now we have the data filled in the response
            return response; // Return the filled AstronomyEvent instance
        }

        // Handle the case where response is null or data is missing
        return null; // Or throw an exception as appropriate
    }


}
