package ryhma5.model.map;

import ryhma5.model.Projections;

public final class SVGMapFactory {

    private SVGMapFactory() {}

    public static SVGMap createMap(Projections projectionType) {
        switch (projectionType) {
            case EQUIRECTANGULAR:
                return createEquirectangularMap();
            case ROBINSON:
                return createRobinsonMap();
            default:
                throw new IllegalArgumentException("Unknown projection type: " + projectionType);
        }
    }

    private static SVGMap createEquirectangularMap() {
        IProjector projector = createProjector(Projections.EQUIRECTANGULAR);
        return new SVGMap("/maps/Equirectangular_projection_world_map_without_borders.svg", projector);
    }

    private static SVGMap createRobinsonMap() {
        IProjector projector = createProjector(Projections.ROBINSON);
        return new SVGMap("/maps/BlankMap_World_simple_Robinson_projection.svg", projector);
    }

    private static IProjector createProjector(Projections projection) {
        switch (projection) {
            case EQUIRECTANGULAR:
                return new EquirectangularProjector();
            default:
                throw new IllegalArgumentException("Unknown projection type: " + projection);
        }
    }
}