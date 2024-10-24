package ryhma5.model;

/**
 * Enum class to define the different map projections available for the application.
 * Maybe some day user could change the projection/map
 */
public enum Projections {
    ROBINSON("/maps/BlankMap_World_simple_Robinson_projection.svg"),
    //EQUIRECTANGULAR("/maps/BlankMap-World-Equirectangular.svg");
    EQUIRECTANGULAR("/maps/Equirectangular_projection_world_map_without_borders.svg");

    private final String mapFilePath;

    // Constructor to initialize the map file path. some java boilerplate idk
    Projections(String mapFilePath) {
        this.mapFilePath = mapFilePath;
    }

    public String getMapFilePath() {
        return mapFilePath;
    }
}
