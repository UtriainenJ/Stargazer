    package ryhma5.model.api.astronomyAPI;

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.net.URLEncoder;
    import java.time.OffsetDateTime;
    import java.util.ArrayList;
    import java.util.Base64;
    import java.util.Locale;

    import com.google.gson.*;
    import ryhma5.model.api.ApiCredentials;
    import ryhma5.model.json.DataManager;

/**
    * A class for handling requests to the Astronomy API.
 */
public class AstronomyHandler {

    private static String APPLICATIONID;
    private static String APPLICATIONSECRET;

    /**
     * Load the API credentials from the data file.
     */
    public static void loadAPICredentials(){
        ApiCredentials credentials = DataManager.loadDataAsObject("api_credentials", ApiCredentials.class);
        APPLICATIONID = credentials.getApplicationId();
        APPLICATIONSECRET = credentials.getApplicationSecret();
    }

    /**
     * Fetches the astronomy event data for the specified body.
     * @param body The name of the body to fetch events for.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param elevation The observer's elevation.
     * @param fromDate The start date for the event data.
     * @param toDate The end date for the event data.
     * @param time The time of day for the event data.
     * @return An ArrayList of AstronomyResponse objects containing the event data.
     * @throws Exception If the request fails.
     */
    public static ArrayList<AstronomyResponse> fetchAstronomyEvent(String body,
                                                                   String latitude,
                                                                   String longitude,
                                                                   String elevation,
                                                                   String fromDate,
                                                                   String toDate,
                                                                   String time) throws Exception {

        String apiUrl = String.format("https://api.astronomyapi.com/api/v2/bodies/events/%s", body);

        String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // Set up the connection
        URL url = new URL(apiUrl + String.format(
                "?latitude=%s&longitude=%s&elevation=%s&from_date=%s&to_date=%s&time=%s&output=%s",
                URLEncoder.encode(latitude, "UTF-8"),
                URLEncoder.encode(longitude, "UTF-8"),
                URLEncoder.encode(elevation, "UTF-8"),
                URLEncoder.encode(fromDate, "UTF-8"),
                URLEncoder.encode(toDate, "UTF-8"),
                URLEncoder.encode(time, "UTF-8"),
                URLEncoder.encode("rows", "UTF-8")));

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

            //System.out.println(response.toString());

            return parseAstronomyEvent(response.toString());
        } else {
            String errorMessage = String.format("Failed to retrieve event data: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    // Parsing function to extract the event data from the response
    private static ArrayList<AstronomyResponse> parseAstronomyEvent(String jsonResponse) {
        ArrayList<AstronomyResponse> events = new ArrayList<>();
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Extract data
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonObject observer = data.getAsJsonObject("observer");

        double observerLongitude = observer.getAsJsonObject("location").get("longitude").getAsDouble();
        double observerLatitude = observer.getAsJsonObject("location").get("latitude").getAsDouble();

        // Extract rows
        JsonArray rows = data.getAsJsonArray("rows");
        for (int i = 0; i < rows.size(); i++) {
            JsonObject row = rows.get(i).getAsJsonObject();
            JsonObject body = row.getAsJsonObject("body");
            String bodyName = body.get("name").getAsString();
            String bodyId = body.get("id").getAsString();

            // Extract events
            JsonArray eventsArray = row.getAsJsonArray("events");
            for (int j = 0; j < eventsArray.size(); j++) {
                JsonObject event = eventsArray.get(j).getAsJsonObject();
                String eventType = event.get("type").getAsString();

                // Extract event highlights
                JsonObject eventHighlights = event.getAsJsonObject("eventHighlights");
                String partialStart = eventHighlights.getAsJsonObject("partialStart").get("date").getAsString();
                String partialEnd = eventHighlights.getAsJsonObject("partialEnd").get("date").getAsString();
                OffsetDateTime dateAndTime = OffsetDateTime.parse(
                        eventHighlights.getAsJsonObject("peak").get("date").getAsString());

                String rise = event.get("rise").getAsString();
                String set = event.get("set").getAsString();

                String totalStart = null;
                String totalEnd = null;
                if (bodyId.equals("sun")){
                    if (!eventHighlights.get("totalStart").isJsonNull()){
                        totalStart = eventHighlights.getAsJsonObject("totalStart").get("date").getAsString();
                        totalEnd = eventHighlights.getAsJsonObject("totalEnd").get("date").getAsString();
                    }
                } else if (bodyId.equals("moon")){
                    if (!eventHighlights.get("fullStart").isJsonNull()){
                        totalStart = eventHighlights.getAsJsonObject("fullStart").get("date").getAsString();
                        totalEnd = eventHighlights.getAsJsonObject("fullEnd").get("date").getAsString();
                    }
                }

                double obscuration = event.getAsJsonObject("extraInfo").get("obscuration").getAsDouble();


                // Building the AstronomyResponse object using the Builder
                AstronomyResponse aResponse = new AstronomyResponse.Builder()
                        .setBodyName(bodyName)
                        .setBodyId(bodyId)
                        .setDateTime(dateAndTime)
                        .setEventType(eventType)
                        .setEventBodyRise(rise)
                        .setEventBodySet(set)
                        .setEventPartialStart(partialStart)
                        .setEventPartialEnd(partialEnd)
                        .setEventTotalStart(totalStart)
                        .setEventTotalEnd(totalEnd)
                        .setEventObscuration(obscuration)
                        .setObserverLongitude(observerLongitude)
                        .setObserverLatitude(observerLatitude)
                        .build();

                events.add(aResponse);
            }
        }

        return events;
    }


    /**
     * Fetches the position data for the specified body.
     * @param body The name of the body to fetch positions for.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param elevation The observer's elevation.
     * @param fromDate The start date for the position data.
     * @param toDate The end date for the position data.
     * @param time The time of day for the position data.
     * @return An ArrayList of AstronomyResponse objects containing the position data.
     * @throws Exception If the request fails.
     */
    public static ArrayList<AstronomyResponse> fetchAstronomyBody(String body,
                                                                  String latitude,
                                                                  String longitude,
                                                                  String elevation,
                                                                  String fromDate,
                                                                  String toDate,
                                                                  String time) throws Exception {

        String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String apiUrl = String.format("https://api.astronomyapi.com/api/v2/bodies/positions/%s", body);

        URL url = new URL(apiUrl + String.format(
                "?latitude=%s&longitude=%s&elevation=%s&from_date=%s&to_date=%s&time=%s&output=%s",
                URLEncoder.encode(latitude, "UTF-8"),
                URLEncoder.encode(longitude, "UTF-8"),
                URLEncoder.encode(elevation, "UTF-8"),
                URLEncoder.encode(fromDate, "UTF-8"),
                URLEncoder.encode(toDate, "UTF-8"),
                URLEncoder.encode(time, "UTF-8"),
                URLEncoder.encode("rows", "UTF-8")
        ));

        // Open the connection
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

            //System.out.println(response.toString());

            return parseAstronomyBody(response.toString());
        } else {
            String errorMessage = String.format("Failed to retrieve data for %s: HTTP response code %d", body ,responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    // Parsing function to extract the position data from the response
    private static ArrayList<AstronomyResponse> parseAstronomyBody(String jsonResponse) {
        ArrayList<AstronomyResponse> responses = new ArrayList<>();
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Extract data
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonArray rows = data.getAsJsonArray("rows");

        JsonObject observer = data.getAsJsonObject("observer");

        double observerLongitude = observer.getAsJsonObject("location").get("longitude").getAsDouble();
        double observerLatitude = observer.getAsJsonObject("location").get("latitude").getAsDouble();

        for (int i = 0; i < rows.size(); i++) {
            JsonObject row = rows.get(i).getAsJsonObject();

            String bodyId = row.getAsJsonObject("body").get("id").getAsString();
            String bodyName = row.getAsJsonObject("body").get("name").getAsString();

            // Extract position information
            for (JsonElement posEntry : row.getAsJsonArray("positions")) {
                JsonObject position = posEntry.getAsJsonObject();

                OffsetDateTime dateAndTime = OffsetDateTime.parse(position.get("date").getAsString());

                String distanceFromEarth = position.getAsJsonObject("distance")
                        .getAsJsonObject("fromEarth").get("km").getAsString();

                String constellation = position.getAsJsonObject("position")
                        .getAsJsonObject("constellation").get("name").getAsString();

                JsonObject positionHorizontal = position.getAsJsonObject("position").getAsJsonObject("horizontal");
                String azimuth = positionHorizontal.getAsJsonObject("azimuth").get("string").getAsString();
                String altitude = positionHorizontal.getAsJsonObject("altitude").get("string").getAsString();

                JsonObject extraInfo = position.getAsJsonObject("extraInfo");

                // elongation and magnitude are not available for earth
                double elongation = 0.0;
                double magnitude = 0.0;
                if (!bodyId.equals("earth")){
                    elongation = extraInfo.get("elongation").getAsDouble();
                    magnitude = extraInfo.get("magnitude").getAsDouble();
                }

                // moon phase only for moon
                String moonPhaseString = null;
                double moonPhaseFraction = 0.0;
                if (extraInfo.has("phase")) {
                    JsonObject moonPhase = extraInfo.getAsJsonObject("phase");
                    moonPhaseString = moonPhase.get("string").getAsString();
                    moonPhaseFraction = moonPhase.get("fraction").getAsDouble();
                }

                // Create AstronomyResponse for this position using builder
                AstronomyResponse aResponse = new AstronomyResponse.Builder()
                        .setBodyId(bodyId)
                        .setBodyName(bodyName)
                        .setDateTime(dateAndTime)
                        .setDistanceFromEarth(distanceFromEarth)
                        .setConstellation(constellation)
                        .setAzimuth(azimuth)
                        .setAltitude(altitude)
                        .setElongation(elongation)
                        .setMagnitude(magnitude)
                        .setMoonPhaseString(moonPhaseString)
                        .setMoonPhaseFraction(moonPhaseFraction)
                        .setObserverLongitude(observerLongitude)
                        .setObserverLatitude(observerLatitude)
                        .build();

                responses.add(aResponse);
            }
        }
        return responses;
    }

    /**
     * Fetches the position data for all celestial bodies.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param elevation The observer's elevation.
     * @param fromDate The start date for the position data.
     * @param toDate The end date for the position data.
     * @param time The time of day for the position data.
     * @return An ArrayList of AstronomyResponse objects containing the position data.
     * @throws Exception If the request fails.
     */
    public static ArrayList<AstronomyResponse> fetchAllBodies(String latitude,
                                                              String longitude,
                                                              String elevation,
                                                              String fromDate,
                                                              String toDate,
                                                              String time) throws Exception {

        String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String apiUrl = "https://api.astronomyapi.com/api/v2/bodies/positions";

        URL url = new URL(apiUrl + String.format(
                "?latitude=%s&longitude=%s&elevation=%s&from_date=%s&to_date=%s&time=%s&output=%s",
                URLEncoder.encode(latitude, "UTF-8"),
                URLEncoder.encode(longitude, "UTF-8"),
                URLEncoder.encode(elevation, "UTF-8"),
                URLEncoder.encode(fromDate, "UTF-8"),
                URLEncoder.encode(toDate, "UTF-8"),
                URLEncoder.encode(time, "UTF-8"),
                URLEncoder.encode("rows", "UTF-8")
        ));

        // Open the connection
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

            // System.out.println(response.toString());

            // Parse the response and create an ArrayList of AstronomyResponse objects
            return parseAstronomyBody(response.toString());
        } else {
            String errorMessage = String.format("Failed to retrieve data for all bodies: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }


    /**
     * Fetches the star chart image for the specified constellation.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param date The date for the star chart.
     * @param constellationId The ID of the constellation to generate the star chart for.
     * @return The URL of the star chart image.
     * @throws Exception If the request fails.
     */
    public static String generateConstellationStarChart(double latitude, double longitude, String date, String constellationId) throws Exception {
        String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String apiUrl = "https://api.astronomyapi.com/api/v2/studio/star-chart";

        String requestBody = String.format(Locale.US,
                "{ \"style\": \"default\", " +
                        "\"observer\": { " +
                        "\"latitude\": %.6f, " +
                        "\"longitude\": %.6f, " +
                        "\"date\": \"%s\" " +
                        "}, " +
                        "\"view\": { " +
                        "\"type\": \"constellation\", " +
                        "\"parameters\": { " +
                        "\"constellation\": \"%s\" " +
                        "} " +
                        "} " +
                        "}",
                latitude, longitude, date, constellationId
        );

        // Open the connection
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        // Write the request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

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

            return parseStarChartResponse(response.toString());
        } else {
            String errorMessage = String.format("Failed to generate star chart: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    // Parsing function to extract the imageUrl from the response
    private static String parseStarChartResponse(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        return jsonObject.getAsJsonObject("data").get("imageUrl").getAsString();
    }

    /**
     * Fetches the star chart image for the specified area.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param date The date for the star chart.
     * @param rightAscension The right ascension of the area.
     * @param declination The declination of the area.
     * @param zoom The zoom level of the star chart.
     * @return The URL of the star chart image.
     * @throws Exception If the request fails.
     */
    public static String generateAreaStarChart(double latitude, double longitude, String date, double rightAscension, double declination, Integer zoom) throws Exception {
        String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String apiUrl = "https://api.astronomyapi.com/api/v2/studio/star-chart";

        String requestBody = String.format(Locale.US,
                "{ " +
                        "\"observer\": { " +
                        "\"latitude\": %.6f, " +
                        "\"longitude\": %.6f, " +
                        "\"date\": \"%s\" " +
                        "}, " +
                        "\"view\": { " +
                        "\"type\": \"area\", " +
                        "\"parameters\": { " +
                        "\"position\": { " +
                        "\"equatorial\": { " +
                        "\"rightAscension\": %.2f, " +
                        "\"declination\": %.2f " +
                        "} " +
                        "}, " +
                        "\"zoom\": %s " +
                        "} " +
                        "} " +
                        "}",
                latitude, longitude, date, rightAscension, declination, zoom
        );

        // Set up the connection
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return parseStarChartResponse(response.toString());
        } else {
            String errorMessage = String.format("Failed to generate star chart: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    private static String parseMoonPhaseResponse(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        return jsonObject.getAsJsonObject("data").get("imageUrl").getAsString();
    }

    /**
     * Fetches the moon phase image for the specified observer.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param date The date for the moon phase.
     * @param format The format of the moon phase image.
     * @return The URL of the moon phase image.
     * @throws Exception If the request fails.
     */
    public static String generateMoonPhaseImage(double latitude, double longitude, String date, String format) throws Exception {
        String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String apiUrl = "https://api.astronomyapi.com/api/v2/studio/moon-phase";

        // Building the request body
        String requestBody = String.format(Locale.US,
                "{ " +
                        "\"format\": \"%s\", " +
                        "\"style\": { " +
                        "\"moonStyle\": \"default\", " +
                        "\"backgroundStyle\": \"stars\", " +
                        "\"backgroundColor\": \"black\", " +
                        "\"headingColor\": \"white\", " +
                        "\"textColor\": \"white\" " +
                        "}, " +
                        "\"observer\": { " +
                        "\"latitude\": %.6f, " +
                        "\"longitude\": %.6f, " +
                        "\"date\": \"%s\" " +
                        "}, " +
                        "\"view\": { " +
                        "\"type\": \"landscape-simple\", " +
                        "\"orientation\": \"north-up\" " +
                        "} " +
                        "}",
                format, latitude, longitude, date
        );

        // Set up the connection
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return parseMoonPhaseResponse(response.toString());
        } else {
            String errorMessage = String.format("Failed to generate moon phase image: HTTP response code %d", responseCode);
            System.err.println(errorMessage);
            throw new Exception(errorMessage);
        }
    }
}