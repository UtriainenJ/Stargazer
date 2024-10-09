package ryhma5.model;

public enum Projections {
    ROBINSON("/maps/BlankMap_World_simple_Robinson_projection.svg"),
    //EQUIRECTANGULAR("/maps/BlankMap-World-Equirectangular.svg");
    EQUIRECTANGULAR("/maps/Equirectangular_projection_world_map_without_borders.svg");

    private final String mapFilePath;

    // Constructor to initialize the map file path
    Projections(String mapFilePath) {
        this.mapFilePath = mapFilePath;
    }

    // Method to get the file path for the projection
    public String getMapFilePath() {
        return mapFilePath;
    }
}
