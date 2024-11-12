    package ryhma5.model;

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

    public class AstronomyAPI {

        /*
        private static final String APPLICATIONID = "a8b19fa3-5fd9-4285-994f-5704eb84baf5";
        private static final String APPLICATIONSECRET = "593aea4dedd5e83e7f58605ab827e97fb2ddc9325769915eec568a2e31aab"
                                                        +"f39d602bc87216783f49ec5d10f1e2877f9343106d473c661d0e76d75ff67"
                                                        +"3587e960e3f7aa6c4870662f4a600cc28abbe0026813a4dd170c84a7820a"
                                                        +"eb543b7f5477641fd74436302c01dd72628c0089cc";
        */

        private static String APPLICATIONID;
        private static String APPLICATIONSECRET;

        public static void loadAPICredentials(){
            DataManager dataManager = new DataManager();
            ApiCredentials credentials = dataManager.loadDataAsObject("api_credentials", ApiCredentials.class);
            APPLICATIONID = credentials.getApplicationId();
            APPLICATIONSECRET = credentials.getApplicationSecret();
        }

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
                String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
                System.err.println(errorMessage);
                throw new Exception(errorMessage);
            }
        }

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
                        totalStart = eventHighlights.getAsJsonObject("totalStart").get("date").getAsString();
                        totalEnd = eventHighlights.getAsJsonObject("totalEnd").get("date").getAsString();
                    } else if (bodyId.equals("moon")){
                        totalStart = eventHighlights.getAsJsonObject("fullStart").get("date").getAsString();
                        totalEnd = eventHighlights.getAsJsonObject("fullEnd").get("date").getAsString();
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
                String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
                System.err.println(errorMessage);
                throw new Exception(errorMessage);
            }
        }

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

                    // Create AstronomyResponse for this position
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

            return responses; // Return the list of responses
        }


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
                String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
                System.err.println(errorMessage);
                throw new Exception(errorMessage);
            }
        }


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