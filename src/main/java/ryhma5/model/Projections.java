package ryhma5.model;

public enum Projections {
    ROBINSON("/maps/BlankMap_World_simple_Robinson_projection.svg"),
    EQUIRECTANGULAR("/maps/Equirectangular_projection_world_map_without_borders.svg");

    private final String mapFilePath;

    Projections(String mapFilePath) {
        this.mapFilePath = mapFilePath;
    }

    public String getMapFilePath() {
        return mapFilePath;
    }
}
