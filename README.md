# Stargazer

## Creators

Eetu Soronen, Jaakko Elomaa Jaakko Utriainen ja Teemu Ranne.

## Description

Stargazer is an app that helps you discover and track celestial events in your area. By selecting your desired location, you can search for various phenomena, such as flyovers of the International Space Station, or explore star maps tailored to your local sky.


## Installation and run

The project has currently been developed and tested using JetBrains' IntelliJ IDEA development environment. Therefore, the installation and usage instructions are provided with the assumption that IntelliJ IDEA is being used. Installation and usage instructions will be updated as the project progresses. For installation instructions for IntelliJ IDEA, visit the following link: https://www.jetbrains.com/help/idea/installation-guide.html .

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

3. Run Stargazer:
Navigate the project folder structure in IntelliJ IDEA and open Start.java.
You can find this file by going to ryhma-5 -> src -> main -> java -> ryhma5.
Once the file is open, click the "Run" button (a green arrow icon) at the top of the IntelliJ IDEA window.

## Usage 

The app is currently in a limited state, but more functionality will be added in the future, and this section will be updated accordingly.

Currently, users can click on the map to add stars. The most recently added star will remain active unless another non-active star is clicked to become active instead. Additionally, you can expand the sidebar by clicking the sidebar button located on the left side of the window.

## Upcoming Features

Generate a star map of the user's location to help users locate celestial events in the night sky.

Get list of celestial events in users location within a selected timeframe.

Track the location of the International Space Station (ISS) by allowing users to see its current position and upcoming flyover times.

