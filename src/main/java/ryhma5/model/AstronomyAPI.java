    package ryhma5.model;

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.net.URLEncoder;
    import java.util.Base64;

    import com.google.gson.*;

    public class AstronomyAPI {

        private static final String APPLICATIONID = "a8b19fa3-5fd9-4285-994f-5704eb84baf5";
        private static final String APPLICATIONSECRET = "593aea4dedd5e83e7f58605ab827e97fb2ddc9325769915eec568a2e31aab"
                                                        +"f39d602bc87216783f49ec5d10f1e2877f9343106d473c661d0e76d75ff67"
                                                        +"3587e960e3f7aa6c4870662f4a600cc28abbe0026813a4dd170c84a7820a"
                                                        +"eb543b7f5477641fd74436302c01dd72628c0089cc";

        public static AstronomyEvent fetchAstronomyEvent(String body,
                                                         String latitude,
                                                         String longitude,
                                                         String elevation,
                                                         String fromDate,
                                                         String toDate,
                                                         String time) throws Exception {

            // Construct the API URL without parameters
            String apiUrl = String.format("https://api.astronomyapi.com/api/v2/bodies/events/%s", body);

            // Base64 encode the APPLICATIONID:APPLICATIONSECRET for Basic Auth
            String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
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

            // System.out.println("API Response: " + jsonResponse);

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


        public static AstronomyBody fetchAstronomyBody(String body,
                                                       String latitude,
                                                       String longitude,
                                                       String elevation,
                                                       String fromDate,
                                                       String toDate,
                                                       String time) throws Exception {

            String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            // Construct the API URL for fetching the position of a singular body
            String apiUrl = String.format("https://api.astronomyapi.com/api/v2/bodies/positions/%s", body);

            // Construct the full URL with the query parameters
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

                // Parse the response and return the result
                return parseAstronomyBody(response.toString());
            } else {
                String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
                System.err.println(errorMessage);
                throw new Exception(errorMessage);
            }
        }

        // Parsing function for the singular body position
        private static AstronomyBody parseAstronomyBody(String jsonResponse) {
            // System.out.println("API Response: " + jsonResponse);

            Gson gson = new Gson();

            // Parse the JSON response into an AstronomyBody object
            AstronomyBody response = gson.fromJson(jsonResponse, AstronomyBody.class);

            // Check if response and data are not null
            if (response != null && response.getData() != null) {
                // Return the parsed data as an AstronomyBody object
                return response;
            }

            // Handle the case where response is null or data is missing
            return null; // Or throw an exception as appropriate
        }

        public static AstronomyBody fetchAllBodies(String latitude,
                                                         String longitude,
                                                         String elevation,
                                                         String fromDate,
                                                         String toDate,
                                                         String time) throws Exception {


            // Combine application ID and secret for authorization
            String auth = APPLICATIONID + ":" + APPLICATIONSECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            // Construct the API URL for fetching the positions of all bodies
            String apiUrl = "https://api.astronomyapi.com/api/v2/bodies/positions";

            // Construct the full URL with query parameters
            URL url = new URL(apiUrl + String.format(
                    "?latitude=%s&longitude=%s&elevation=%s&from_date=%s&to_date=%s&time=%s&output=%s",
                    URLEncoder.encode(latitude, "UTF-8"),
                    URLEncoder.encode(longitude, "UTF-8"),
                    URLEncoder.encode(elevation, "UTF-8"),
                    URLEncoder.encode(fromDate, "UTF-8"),
                    URLEncoder.encode(toDate, "UTF-8"),
                    URLEncoder.encode(time, "UTF-8"),
                    URLEncoder.encode("rows", "UTF-8")  // Using "rows" as the output format
            ));

            // Open the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                // Read the response from the input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                // Parse the response and return the list of bodies
                return parseAstronomyBody(response.toString());

            } else {
                String errorMessage = String.format("Failed to retrieve data: HTTP response code %d", responseCode);
                System.err.println(errorMessage);
                throw new Exception(errorMessage);
            }

        }


    }