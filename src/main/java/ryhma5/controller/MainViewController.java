package ryhma5.controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.util.Duration;
import ryhma5.model.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MainViewController {

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox sidebar;

    @FXML
    private HBox sidebarContainer;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private ContextMenu suggestionsMenu;

    @FXML
    private Pane mapPane;

    @FXML
    private VBox eventContainer;

    private boolean isSidebarVisible = false;

    // textfield suggestions variables
    private List<City> cityList;
    private ScheduledService<Void> searchService;
    private PauseTransition pause;

    // map variables
    private final Projections PROJECTION = Projections.EQUIRECTANGULAR;
    private SVGMap svgMap;
    private ImageView mapImageView;



    public void initialize() {
        // Sidebar is moved out of the way at start
        if (sidebar != null) {
            sidebarContainer.setTranslateX(-sidebar.getPrefWidth());
        } else {
            System.out.println("Sidebar is not loaded.");
        }

        loadCities();
        intializeContextMenu();
        setDefaultDates();
        initializeMap();
    }

    private void initializeMap() {
        svgMap = SVGMapFactory.createMap(PROJECTION);

        mapImageView = svgMap.loadMap(mapPane);
        if (mapImageView != null) {
            mapPane.getChildren().add(mapImageView);

            mapImageView.setPickOnBounds(true);
            mapImageView.setOnMouseClicked(this::handleMapClick);
        }

        // Reposition markers when the window is resized
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> svgMap.updateMarkers(mapImageView));
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> svgMap.updateMarkers(mapImageView));
    }

    private void handleMapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        System.out.println("Clicked X: " + x + ", Y: " + y);

        svgMap.addMarker(x, y, mapImageView, mapPane);

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        double[] latLong = svgMap.getLatLongFromXY(x, y, imageWidth, imageHeight);

        System.out.println("Latitude: " + latLong[0] + ", Longitude: " + latLong[1]);

        CompletableFuture.runAsync(() -> sendAPIRequests(latLong[0], latLong[1]));

    }

    private void sendAPIRequests(double x, double y) {

        System.out.println("---------------------------- API TEST ------------------------------------");
        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwww    EVENTS    wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");

        AstronomyController avm = new AstronomyController();

        ArrayList<AstronomyResponse> testEventList = avm.getAstronomyEvent(
                "moon", Double.toString(x), Double.toString(y), "10",
                "2023-11-07", "2024-10-08", "12:00:00");
        for (AstronomyResponse evt : testEventList) {
            System.out.println(evt.toString());
        }

        System.out.println("SIZE: " + testEventList.size());

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxx       BODIES       xxxxxxxxxxxxxxxxxxxxxxxxxx");
        String bodyIdToTestFor = "moon";
        ArrayList<AstronomyResponse> testBodyList = avm.getAstronomyBody(
                bodyIdToTestFor, Double.toString(x), Double.toString(y), "10",
                "2024-09-25", "2024-09-28", "01:00:00");
        for (AstronomyResponse body : testBodyList) {
            System.out.println(body.toString());
        }

        ArrayList<AstronomyResponse> testBodyList2 = avm.getAllAstronomyBodies(Double.toString(x), Double.toString(y),
                "10", "2024-10-07", "2024-10-08", "12:00:00");
        for (AstronomyResponse body : testBodyList2) {
            System.out.println(body.toString());
        }

        //String constellationChartURL = avm.getConstellationStarChart(latLong[0], latLong[1],"2024-10-07", "ori");
        //System.out.println(constellationChartURL);
        String areaChartURL = avm.getAreaStarChart(x, y, "2024-10-07", 14.83, -15.23, 9);
        System.out.println(areaChartURL);

        String moonPictureURL = avm.getMoonPhaseImage(x,y,"2024-10-07","png");
        System.out.println(moonPictureURL);

        System.out.println("ooooooooooooooooooooooooooo     ISS    ooooooooooooooooooooooooooooooooooo");

        WhereISSController issVM = new WhereISSController();

        ISSResponse issTest = issVM.getISS("kilometers", WhereISSAPI.dateToTimestamp("2024-10-07"));
        System.out.println("ISS velocity at 2024-10-7: " + issTest.getVelocity());

        ArrayList<Long> issTestDates = new ArrayList<>();
        issTestDates.add(WhereISSAPI.dateToTimestamp("2024-10-07"));
        issTestDates.add(WhereISSAPI.dateToTimestamp("2024-10-08"));
        List<ISSResponse> issTestsList = issVM.getISSPositions(issTestDates, "kilometers");
        System.out.println("ISS altitude from get positions list: " + issTestsList.get(1).getAltitude());
        System.out.println("---------------------------------------------------------------------------");
    }

    // These could be modified to make sure the sidebar containers don't block the map from the cursor. Couldn't get it to work simply yet

    //sidebarContainer.setMouseTransparent(true);
    //    for (Node child : sidebarContainer.getChildrenUnmodifiable()){
    //       child.setMouseTransparent(false);
    //}


    @FXML
    private void handleEventSearch() {

        // pick dates and city from the fields
        LocalDate startDate = datePickerStart.getValue();
        LocalDate endDate = datePickerEnd.getValue();
        String city = searchField.getText();
        double lat = 60.454510; // default to turku
        double lng = 22.264824;

        City selectedCity = cityList.stream()
                .filter(c -> c.getName().equals(city))
                .findFirst()
                .orElse(null);
        if (selectedCity != null) {
            lat = Double.parseDouble(selectedCity.getLat());
            lng = Double.parseDouble(selectedCity.getLng());
            System.out.println("Selected city: " + selectedCity.getName() + " (" + selectedCity.getLat() + ", " + selectedCity.getLng() + ")");
        } else {
            System.out.println("City not found: " + city);
        }

        svgMap.addMarkerByCoordinates(lat, lng, mapImageView, mapPane);

        // PLACEHOLDER FOR ADDING EVENTS TO LIST
        addEventCard("/icons/meteor.png", "METEORS",
                OffsetDateTime.of(2024, 11, 6, 14, 0, 0, 0, ZoneOffset.ofHours(3)));
        addEventCard("/icons/stars.png", "METEORS",
                OffsetDateTime.of(2024, 11, 26, 17, 30, 0, 0, ZoneOffset.ofHours(3)));
        addEventCard("/icons/darkstar.png", "METEORS",
                OffsetDateTime.of(2025, 1, 4, 22, 0, 0, 0, ZoneOffset.ofHours(3)));
    }

    private void intializeContextMenu() {
        searchService = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        Platform.runLater(MainViewController.this::updateSearchSuggestions);
                        return null;
                    }
                };
            }
        };
        searchService.setDelay(Duration.millis(300)); // Adjust debounce delay as needed
        searchService.setPeriod(Duration.INDEFINITE); // Run only once after each key event

        // Initialize the debouncing PauseTransition
        pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(event -> updateSearchSuggestions());
    }

    @FXML
    public void handleSearchFieldKeyPress(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleEventSearch();
            return;
        }

        // Reset the PauseTransition to delay the updateSuggestions call
        pause.playFromStart();
    }

    private void updateSearchSuggestions() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            suggestionsMenu.hide();
            return;
        }

        List<City> filteredCities = cityList.parallelStream()
                .filter(city -> city.getName().toLowerCase().startsWith(query))
                .limit(20)
                .collect(Collectors.toList());

        suggestionsMenu.getItems().clear();
        filteredCities.forEach(city -> {
            MenuItem item = new MenuItem(city.getName());
            item.setOnAction(e -> {
                searchField.setText(city.getName());
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
        CompletableFuture.supplyAsync(() -> {
            DataManager dataManager = new DataManager();
            return dataManager.loadCityList("cities_pruned");
        }).thenAccept(cityList -> {
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

        if (datePickerEnd.getValue() == null) {
            datePickerEnd.setValue(currentDate.plusDays(7));
        }
    }

    @FXML
    public void handleDatePickStart() {
        LocalDate startDate = datePickerStart.getValue();
        LocalDate endDate = datePickerEnd.getValue();

        if (endDate != null && startDate.isAfter(endDate)) {
            datePickerStart.setValue(endDate);
        } else {
            System.out.println("handleDatePickStart: " + startDate);
        }
    }

    @FXML
    public void handleDatePickEnd() {
        LocalDate startDate = datePickerStart.getValue();
        LocalDate endDate = datePickerEnd.getValue();

        if (startDate != null && endDate.isBefore(startDate)) {
            datePickerEnd.setValue(startDate);
        } else {
            System.out.println("handleDatePickEnd: " + endDate);
        }
    }

    @FXML
    public void addEventCard(String mainImagePath, String titleText, OffsetDateTime eventDateTime) {
        String buttonImagePath = "/icons/stars.png";

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
        Text title = new Text(titleText);
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

        // Button with ImageView inside
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

        // Add all components to the AnchorPane
        anchorPane.getChildren().addAll(mainImageView, title, date, time, iconButton);

        // Add the AnchorPane to the VBox container
        eventContainer.getChildren().add(anchorPane);
    }
}