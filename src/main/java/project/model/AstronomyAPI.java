package project.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class AstronomyAPI {

    public static AstronomyEvent fetchAstronomyEvent(String latitude, String longitude, String elevation, String date, String time) throws Exception {
        String applicationId = "a8b19fa3-5fd9-4285-994f-5704eb84baf5";
        String applicationSecret = "593aea4dedd5e83e7f58605ab827e97fb2ddc9325769915eec568a2e31aabf39d602bc87216783f49ec5d10f1e2877f9343106d473c661d0e76d75ff673587e960e3f7aa6c4870662f4a600cc28abbe0026813a4dd170c84a7820aeb543b7f5477641fd74436302c01dd72628c0089cc";

        String apiUrl = String.format(
                "https://api.astronomyapi.com/api/v2/bodies/events/sun?latitude=%s&longitude=%s&elevation=%s&from_date=%s&to_date=%s&time=%s",
                latitude, longitude, elevation, date, date, time);

        // Base64 encode the applicationId:applicationSecret
        String auth = applicationId + ":" + applicationSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        URL url = new URL(apiUrl);
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

            // return an AstronomyEvent object with the provided parameters
            return new AstronomyEvent(latitude, longitude, elevation, date, time);

        } else {
            throw new Exception("Failed to retrieve data: HTTP response code " + responseCode);
        }
    }
}