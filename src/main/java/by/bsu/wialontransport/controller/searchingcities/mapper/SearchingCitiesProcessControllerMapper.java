package by.bsu.wialontransport.controller.searchingcities.mapper;

import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.AreaCoordinateRequest;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.CoordinateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

@Component
@RequiredArgsConstructor
public final class SearchingCitiesProcessControllerMapper {
    private final GeoJSONWriter geoJSONWriter;

    public SearchingCitiesProcessResponse mapToResponse(final SearchingCitiesProcess process) {
        return SearchingCitiesProcessResponse.builder()
                .id(process.getId())
                .bounds(mapBounds(process))
                .searchStep(process.getSearchStep())
                .totalPoints(process.getTotalPoints())
                .handledPoints(process.getHandledPoints())
                .status(process.getStatus())
                .build();
    }

    //TODO: test
    public AreaCoordinate mapToAreaCoordinate(final AreaCoordinateRequest request) {
        final Coordinate leftBottom = mapToCoordinate(request.getLeftBottom());
        final Coordinate rightUpper = mapToCoordinate(request.getRightUpper());
        return new AreaCoordinate(leftBottom, rightUpper);
    }

    private Geometry mapBounds(final SearchingCitiesProcess source) {
        return geoJSONWriter.write(source.getBounds());
    }

    private static Coordinate mapToCoordinate(final CoordinateRequest request) {
        return new Coordinate(request.getLatitude(), request.getLongitude());
    }
}
