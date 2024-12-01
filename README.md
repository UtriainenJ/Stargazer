# Stargazer

## Creators

Eetu Soronen, Jaakko Elomaa, Jaakko Utriainen & Teemu Ranne.

## Description

Stargazer is an app that helps you discover and track celestial events in your area. By selecting your desired location, you can search for various phenomena, such as flyovers of the International Space Station, or explore star maps tailored to your local sky.


## Installation and run

The project has currently been developed and tested using JetBrains' IntelliJ IDEA development environment. Therefore, the installation and usage instructions are provided with the assumption that IntelliJ IDEA is being used. Installation and usage instructions will be updated as the project progresses. For installation instructions for IntelliJ IDEA, visit the following link: https://www.jetbrains.com/help/idea/installation-guide.html .

Remember, while configuring IntelliJ during installation, choose to create associations between IntelliJ and .java and .pom files. Its also good to update the PATH variables same time.

The project uses Java 17, JavaFX 23 and Maven 3.9.9. Instructions for installing Java can be found at the following link:https://docs.oracle.com/en/java/javase/17/install/installation-jdk-microsoft-windows-platforms.html. 
If you cannot install Java 17, Java 22 will also work.

You can find instructions for installing JavaFX at the following link: https://openjfx.io/openjfx-docs//#introduction. If you know what you are doing, you can download JavaFX 23 directly from here: https://gluonhq.com/products/javafx/.

IntelliJ IDEA includes a built-in Maven 3 version, which should be sufficient for most cases. However, if for some reason the built-in Maven is not enough, you can find instructions for installing Maven separately here:https://maven.apache.org/install.html.

1. Clone the repository:
Select or create folder to clone project with following command.

    git clone https://course-gitlab.tuni.fi/compse110-fall2024/ryhma-5.git

2. Open project in IntelliJ IDEA:
Open IntelliJ IDEA and click on the main menu in the top left corner.
Hover over "File," then select "Open" from the dropdown menu (second option).
In the "Open File or Project" window, select the cloned project and click "OK."

3. The dependencies of of this project are managed with Maven. In IntelliJ GUI they can be installed by right clicking pom.xml and selecting "Add as Maven Project" or "reload project" if the project is already open.

4. Run Stargazer:
Navigate the project folder structure in IntelliJ IDEA and open Start.java.
You can find this file by going to ryhma-5 -> src -> main -> java -> ryhma5.
Once the file is open, click the "Run" button (a green arrow icon) at the top of the IntelliJ IDEA window.

It is possible that the JDK is not fully configured to work with IntelliJ yet. To resolve this, go to File -> Project Structure -> SDKs (under Platform Settings), click the '+' icon to add a new SDK, and look under "Detected SDKs" for your installed JDK. Select it, then click "Apply".
Alternative: After cloning the project, open it, and you may see a pop-up with the following warning: 'JDK "temurin-17" is missing — download Eclipse Temurin.'( You may need to open a Java file first to see this message. ) From this pop-up, you can update IntelliJ to include the JDK.

## Usage 

When the app is launched for the first time, the main window will open. This window includes a map as its primary display, with a sidebar button located on the left-hand side.

On the map, there is an icon representing the current location of the International Space Station (ISS). The ISS's position updates in real-time as it moves.

Click the sidebar button on the left to expand the sidebar.

There are two ways to get location:

1. Enter a city name or location coordinates into the sidebar text field.

2. Click directly on the map to select a location.

Once a location is selected, click the "Search" button to retrieve celestial events for that area.

The app will display celestial events, such as visible planets and phenomena like solar eclipses, based on the chosen location.

The map will place a star marker to represent the selected location. Users can have up to three location markers on the map at a time. The active marker is animated to spin. To change the active marker, click on one of the inactive markers. Swapping the active marker will update the text field, allowing you to retrieve events for that location by clicking the 'Search' button.

Event cards contain details about the event, such as the event name, date, time, and a star map button. You can click the star map button to view a star map from the event's location.

From the second launch onward, the app will load all previous user data, including map markers, selected city/location, and date, and will update the UI accordingly.
