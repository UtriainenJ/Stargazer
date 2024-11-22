package ryhma5.model.map;

public final class SVGMapFactory {

    private SVGMapFactory() {
    }

    public static SVGMap createMap(Projections projectionType) {
        if (projectionType == null) {
            throw new IllegalArgumentException("Unknown projection type: null");
        }

        switch (projectionType) {
            case EQUIRECTANGULAR:
                return createEquirectangularMap();
//            case ROBINSON: // not implemented
//                return createRobinsonMap();
            default:
                throw new IllegalArgumentException("Unknown projection type: " + projectionType);
        }
    }

    private static SVGMap createEquirectangularMap() {
        IProjector projector = createProjector(Projections.EQUIRECTANGULAR);
        return new SVGMap("/maps/Equirectangular_projection_world_map_without_borders.svg", projector);
    }

    private static SVGMap createRobinsonMap() {
        throw new UnsupportedOperationException("Robinson projection is not implemented yet");
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