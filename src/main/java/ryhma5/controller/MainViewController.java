package ryhma5.controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ryhma5.model.api.astronomyAPI.AstronomyResponse;
import ryhma5.model.api.astronomyAPI.AstronomySorter;
import ryhma5.model.api.astronomyAPI.StarChartProxy;
import ryhma5.model.dateTimeUtils.DateShifter;
import ryhma5.model.dateTimeUtils.LocalDateConverter;
import ryhma5.model.json.City;
import ryhma5.model.json.DataManager;
import ryhma5.model.json.UserPreferences;


public class MainViewController {

    private MapController mapController;
    private AstronomyController astronomyController;
    @FXML
    private VBox sidebar;

    @FXML
    private HBox sidebarContainer;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private ContextMenu suggestionsMenu;

    @FXML
    private VBox eventContainer;

    private boolean isSidebarVisible = false;

    @FXML
    private Pane mapPane;
    @FXML
    private ImageView mapImageView;

    // user preferences variables
    private UserPreferences userPreferencesLoadData = null;

    // textfield suggestions variables
    private List<City> cityList;

    private WhereISSController issController;

    public void initialize() {
        // Sidebar is moved out of the way at start
        if (sidebar != null) {
            sidebarContainer.setTranslateX(-sidebar.getPrefWidth());
        } else {
            System.out.println("Sidebar is not loaded.");
        }

        loadUserPreferences();
        loadCities();
        setDefaultDates();

        mapController = new MapController(this, mapPane, mapImageView);

        mapController.initializeMap();

        astronomyController = new AstronomyController();
    }

    void setSearchField(String data) {
        searchField.setText(data);
    }

    TextField getSearchField() {
        return searchField;
    }

    public void setISSController(WhereISSController issController) {
        this.issController = issController;
        issController.setMapImageView(mapController.mapImageView);


        // Add ISS image to mapPane
        ImageView issImageView = issController.getISSImageView();
        if (!mapController.mapPane.getChildren().contains(issImageView)) {
            mapController.mapPane.getChildren().add(issImageView);
        }

        issController.startPeriodicISSUpdate();
        issController.updateISSPosition();
    }

    void handleMapClick(MouseEvent event) {
        mapController.handleMapClick(event);
    }

    ArrayList<AstronomyResponse> sendAPIRequests(double x, double y, String date) {

        // Length of the timeframe
        int monthsToShift = 6;
        String toDate = DateShifter.shiftDateByMonths(date, monthsToShift);
        String time = "12:00:00";


        ArrayList<AstronomyResponse> moonEventList = astronomyController.getAstronomyEvent(
                "moon", Double.toString(x), Double.toString(y), "10",
                date, toDate, time);
        ArrayList<AstronomyResponse> sunEventList = astronomyController.getAstronomyEvent(
                "sun", Double.toString(x), Double.toString(y), "10",
                date, toDate, time);



        ArrayList<AstronomyResponse> bodyList = astronomyController.getAllAstronomyBodies(Double.toString(x), Double.toString(y),
                "10", date, toDate, time);

        // Remove certain bodies
        AstronomySorter.removeBodies(bodyList,
                "earth",
                "pluto",
                "neptune",
                "uranus",
                "mercury",
                "sun");

        // Get only the dates where the body has the strongest magnitude
        ArrayList<AstronomyResponse> brightestBodiesList = AstronomySorter.getBrightestBodies(bodyList);

        // Combine all the lists
        ArrayList<AstronomyResponse> eventList = new ArrayList<>();
        eventList.addAll(brightestBodiesList);
        if (moonEventList != null){
            eventList.addAll(moonEventList);
        }
        if (sunEventList != null){
            eventList.addAll(sunEventList);
        }

        // Sort the list by date (using dateTime as the key for sorting)
        eventList.sort(Comparator.comparing(AstronomyResponse::getDateTime));

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxx       ALL EVENTS       xxxxxxxxxxxxxxxxxxxxxxxxxx");
        for (AstronomyResponse event : eventList) {
            System.out.println(event.toString());
        }
        
        System.out.println("---------------------------------------------------------------------------");

        return eventList;
    }

    @FXML
    private void handleEventSearch() {
        // Pick dates from the date picker
        LocalDate startDate = datePickerStart.getValue();
        String input = searchField.getText().trim();

        // Check if the input is in coordinate format
        if (input.contains(",")) {
            // Try parsing coordinates from the input in the format "x.xx, y.yy"
            try {
                String[] coordinates = input.split(",");
                if (coordinates.length == 2) {
                    // Parse latitude and longitude from the string
                    double lat = Double.parseDouble(coordinates[0].trim());
                    double lng = Double.parseDouble(coordinates[1].trim());

                    // Validate the coordinates
                    if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
                        showErrorMessage("Coordinates are out of bounds");
                        return; // Stop further processing
                    }

                    System.out.println("Coordinates entered: (" + lat + ", " + lng + ")");

                    // Add marker to the map
                    mapController.map.addMarkerByCoordinates(lat, lng, mapController.mapImageView, mapController.mapPane, searchField);

                    // Clear the event container while loading
                    eventContainer.getChildren().clear();
                    Text loadingText = new Text("Loading events...");
                    loadingText.setFont(Font.font("Unispace-Bold", 24));
                    loadingText.setFill(Color.WHITE);
                    eventContainer.getChildren().add(loadingText);

                    // Run API request in a background thread
                    CompletableFuture.supplyAsync(() ->
                            sendAPIRequests(lat, lng, startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    ).thenAccept(eventList -> {
                        // Update UI on JavaFX Application Thread
                        Platform.runLater(() -> {
                            eventContainer.getChildren().clear(); // Clear the loading message
                            if (eventList != null && !eventList.isEmpty()) {
                                for (AstronomyResponse event : eventList) {
                                    addEventCard(event, lat, lng);
                                }
                            } else {
                                showErrorMessage("No events found for these coordinates.");
                            }
                        });
                    }).exceptionally(ex -> {
                        // Handle exceptions on JavaFX Application Thread
                        Platform.runLater(() -> {
                            eventContainer.getChildren().clear();
                            Text errorText = new Text("Error loading events");
                            errorText.setFont(Font.font("Unispace-Bold", 20));
                            errorText.setFill(Color.RED);
                            eventContainer.getChildren().add(errorText);
                        });
                        ex.printStackTrace(); // Safely handle and log the error
                        return null;
                    });
                } else {
                    // If the coordinates format is incorrect
                    showErrorMessage("Invalid coordinates format");
                }
            } catch (NumberFormatException e) {
                // Handle invalid coordinate format
                showErrorMessage("Invalid coordinates format");
            }
        } else {
            // If it's not coordinates, assume it's a city name
            Optional<City> selectedCity = cityList.stream()
                    .filter(c -> c.getCityName().equalsIgnoreCase(input))
                    .findFirst();

            if (selectedCity.isPresent()) {
                double lat = Double.parseDouble(selectedCity.get().getLat());
                double lng = Double.parseDouble(selectedCity.get().getLng());
                System.out.println("Selected city: " + selectedCity.get().getCityName() + " (" + lat + ", " + lng + ")");

                // Add marker to the map
                mapController.map.addMarkerByCoordinates(lat, lng, mapController.mapImageView, mapController.mapPane, searchField);

                // Clear the event container while loading
                eventContainer.getChildren().clear();
                Text loadingText = new Text("Loading events...");
                loadingText.setFont(Font.font("Unispace-Bold", 24));
                loadingText.setFill(Color.WHITE);
                eventContainer.getChildren().add(loadingText);

                // Run API request in a background thread
                CompletableFuture.supplyAsync(() ->
                        sendAPIRequests(lat, lng, startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                ).thenAccept(eventList -> {
                    // Update UI on JavaFX Application Thread
                    Platform.runLater(() -> {
                        eventContainer.getChildren().clear(); // Clear the loading message
                        if (eventList != null && !eventList.isEmpty()) {
                            for (AstronomyResponse event : eventList) {
                                addEventCard(event, lat, lng);
                            }
                        } else {
                            showErrorMessage("No events found for the selected city.");
                        }
                    });
                }).exceptionally(ex -> {
                    // Handle exceptions on JavaFX Application Thread
                    Platform.runLater(() -> {
                        eventContainer.getChildren().clear();
                        Text errorText = new Text("Error loading events. Please try again.");
                        errorText.setFont(Font.font("Unispace-Bold", 20));
                        errorText.setFill(Color.RED);
                        eventContainer.getChildren().add(errorText);
                    });
                    ex.printStackTrace(); // Safely handle and log the error
                    return null;
                });
            } else {
                System.out.println("City not found: " + input);
                showErrorMessage("City not found. Please check your input.");
            }
        }
    }

    // Helper method to display error messages
    private void showErrorMessage(String message) {
        Platform.runLater(() -> {
            eventContainer.getChildren().clear();
            Text errorText = new Text(message);
            errorText.setFont(Font.font("Unispace-Bold", 18));
            errorText.setFill(Color.RED);
            eventContainer.getChildren().add(errorText);
        });
    }


    @FXML
    public void handleSearchFieldKeyPress(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleEventSearch();
            return;
        }
        updateSearchSuggestions();
    }

    private void updateSearchSuggestions() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            suggestionsMenu.hide();
            return;
        }

        List<City> filteredCities = cityList.parallelStream()
                .filter(city -> city.getCityName().toLowerCase().startsWith(query))
                .limit(20)
                .toList();

        suggestionsMenu.getItems().clear();
        filteredCities.forEach(city -> {
            MenuItem item = new MenuItem(city.getCityName());
            item.setOnAction(e -> {
                searchField.setText(city.getCityName());
                searchField.positionCaret(searchField.getText().length());
                suggestionsMenu.hide();
            });
            suggestionsMenu.getItems().add(item);
        });

        if (!filteredCities.isEmpty()) {
            double screenX = searchField.localToScreen(searchField.getBoundsInLocal()).getMinX();
            double screenY = searchField.localToScreen(searchField.getBoundsInLocal()).getMaxY();
            suggestionsMenu.show(searchField, screenX, screenY);
        } else {
            suggestionsMenu.hide();
        }
    }

    @FXML
    public void toggleSidebar() {
        TranslateTransition slideTransition = new TranslateTransition();
        slideTransition.setNode(sidebarContainer);
        slideTransition.setDuration(Duration.millis(300));

        if (isSidebarVisible) {
            // Hide the sidebar
            slideTransition.setToX(-sidebar.getPrefWidth());
        } else {
            // Show the sidebar
            slideTransition.setToX(0);
        }

        // Play the animation
        slideTransition.play();

        // Toggle the state
        isSidebarVisible = !isSidebarVisible;
    }

    // load cities json asynchronously, since it can be large
    private void loadCities() {
        CompletableFuture.supplyAsync(() -> DataManager.loadDataAsList("cities_pruned", City.class)).thenAccept(cityList -> {
            Platform.runLater(() -> {
                System.out.println("Cities loaded: " + cityList.size());
                this.cityList = cityList;
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    /**
     * If the date pickers are empty (on init), set them to the current date.
     */
    public void setDefaultDates() {
        LocalDate currentDate = LocalDate.now();

        if (datePickerStart.getValue() == null) {
            datePickerStart.setValue(currentDate);
        }
    }

    /**
     * load user preferences from json and update UI
     */
    private void loadUserPreferences() {
        userPreferencesLoadData = DataManager.loadDataAsObject("user_preferences", UserPreferences.class);

        if (userPreferencesLoadData == null) {
            System.out.println("No user prefrences found");
            return;
        }
        searchField.setText(userPreferencesLoadData.getTextFieldText());
        datePickerStart.setValue(LocalDateConverter.fromString(userPreferencesLoadData.getDateStart()));

        Platform.runLater(()->{
            handleEventSearch();
        });
    }

    /**
     * save user preferences, map markers and astronomy response data as json
     */
    public  void saveUserData(){
        // save user preferences
        UserPreferences userPreferences = new UserPreferences(
                searchField.getText(),
                LocalDateConverter.toString(datePickerStart.getValue())
        );
        DataManager.saveData(userPreferences, "user_preferences");

        // call mapController to save map markers
        mapController.saveMapMarkers();
        // call astronomyController to save astronomy response events and urls
        astronomyController.saveAstronomyData();
    }

    public WhereISSController getIssController() {
        return issController;
    }

    @FXML
    public void addEventCard(AstronomyResponse event, double latitude, double longitude) {
        String eventName = event.getEventType();
        OffsetDateTime eventDateTime = event.getDateTime();

        eventName = (eventName == null) ? event.getBodyName() : switch (eventName) {
            case "total_lunar_eclipse" -> "Lunar Eclipse";
            case "total_solar_eclipse" -> "Total Solar Eclipse";
            case "annular_solar_eclipse" -> "Annular Solar Eclipse";
            case "partial_solar_eclipse" -> "Partial Solar Eclipse";
            case "partial_lunar_eclipse" -> "Partial Lunar Eclipse";
            case "penumbral_lunar_eclipse" -> "Penumbral Lunar Eclipse";
            default -> event.getBodyName(); // Fallback to bodyName if eventName doesn't match
        };

        // Image for the starmap button
        String buttonImagePath = "/icons/stars.png";
        String mainImagePath;

        // Determine the main image path based on the event name using a switch statement
        switch (eventName.toLowerCase()) {
            case "moon":
                mainImagePath = "/icons/meteor.png";
                break;
            case "jupiter":
                mainImagePath = "/icons/saturn.png";
                break;
            case "mars":
                mainImagePath = "/icons/saturn.png";
                break;
            case "saturn":
                mainImagePath = "/icons/saturn.png";
                break;
            case "venus":
                mainImagePath = "/icons/saturn.png";
                break;
            case "lunar eclipse":
                mainImagePath = "/icons/darkstar.png";
                break;
            case "total solar eclipse":
                mainImagePath = "/icons/darkstar.png";
                break;
            // Add more cases for other celestial bodies as needed
            default:
                mainImagePath = "/icons/stars.png";  // Fallback image in case the body name is not recognized
                break;
        }

        // Format the OffsetDateTime to display date and time separately
        String formattedDate = eventDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String formattedTime = eventDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));


        // Create the AnchorPane container
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 300);
        anchorPane.setMinHeight(300);

        // Alternating background colors
        int index = eventContainer.getChildren().size();
        Color bgColor = (index % 2 == 0) ? Color.web("#9168c7") : Color.web("#9c78d2");
        anchorPane.setBackground(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));

        // Main ImageView
        ImageView mainImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(mainImagePath))));
        mainImageView.setFitWidth(120);
        mainImageView.setFitHeight(129);
        AnchorPane.setLeftAnchor(mainImageView, 40.0);
        AnchorPane.setTopAnchor(mainImageView, 40.0);

        // Title Text
        Text title = new Text(eventName);
        title.setFont(Font.font("Unispace-Bold", 36));
        title.setFill(Color.WHITE);
        AnchorPane.setLeftAnchor(title, 40.0);
        AnchorPane.setBottomAnchor(title, 40.0);

        // Date Text
        Text date = new Text(formattedDate);  // Use the formatted date part
        date.setFont(Font.font("Unispace-Bold", 40));
        date.setFill(Color.WHITE);
        AnchorPane.setTopAnchor(date, 20.0);
        AnchorPane.setRightAnchor(date, 45.0);

        // Time Text
        Text time = new Text(formattedTime);  // Use the formatted time part
        time.setFont(Font.font("Unispace-Bold", 40));
        time.setFill(Color.WHITE);
        AnchorPane.setTopAnchor(time, 78.0);
        AnchorPane.setRightAnchor(time, 45.0);

        // Add the button with ImageView only if the event is not a special event type (like an eclipse)
        if (event.getEventType() == null) {
            // Button with ImageView inside (for non-event types like celestial bodies)
            Button iconButton = new Button();
            iconButton.setPrefSize(120, 120);
            iconButton.setStyle(
                    "-fx-background-color: #f2edf8; " +
                            "-fx-background-radius: 100; " +
                            "-fx-border-color: #34125f; " +
                            "-fx-border-radius: 100; " +
                            "-fx-border-width: 5; " +
                            "-fx-cursor: hand;");
            AnchorPane.setBottomAnchor(iconButton, 30.0);
            AnchorPane.setRightAnchor(iconButton, 50.0);

            ImageView buttonImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(buttonImagePath))));
            buttonImageView.setFitWidth(91);
            buttonImageView.setFitHeight(87);
            iconButton.setGraphic(buttonImageView);

            // open star chart on click
            iconButton.setOnAction(e -> openStarChart(event, latitude, longitude));

            // Add the button to the AnchorPane
            anchorPane.getChildren().add(iconButton);
        }

        // Add all components to the AnchorPane
        anchorPane.getChildren().addAll(mainImageView, title, date, time);

        // Add the AnchorPane to the VBox container
        eventContainer.getChildren().add(anchorPane);
    }

    private void openStarChart(AstronomyResponse event, double latitude, double longitude) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Makes the popup modal, blocking other windows
        popupStage.setTitle("Star Chart");

        // Create an ImageView to display the placeholder and final image
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(false); // Disable aspect ratio preservation to fill the window
        imageView.fitWidthProperty().bind(popupStage.widthProperty());
        imageView.fitHeightProperty().bind(popupStage.heightProperty());

        // Create a layout to contain the ImageView
        StackPane popupContent = new StackPane(imageView);

        // Set the popup content in a scene and display it
        Scene popupScene = new Scene(popupContent, 600, 600);

        popupScene.setOnKeyPressed(eventKey -> {
            if (eventKey.getCode() == KeyCode.ESCAPE) {
                popupStage.close(); // Close the popup when ESC is pressed
            }
        });

        popupStage.setScene(popupScene);
        popupStage.show();

        // Initialize StarChartProxy, which will handle loading the image asynchronously
        new StarChartProxy(imageView, astronomyController, event);
    }

}