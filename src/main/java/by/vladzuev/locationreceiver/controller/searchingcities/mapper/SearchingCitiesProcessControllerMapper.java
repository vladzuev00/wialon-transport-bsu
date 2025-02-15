package by.vladzuev.locationreceiver.controller.searchingcities.mapper;

import by.vladzuev.locationreceiver.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.vladzuev.locationreceiver.crud.dto.SearchingCitiesProcess;
import by.vladzuev.locationreceiver.model.AreaCoordinate;
import by.vladzuev.locationreceiver.model.AreaCoordinateRequest;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.model.CoordinateRequest;
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

    public AreaCoordinate mapToAreaCoordinate(final AreaCoordinateRequest request) {
        final GpsCoordinate leftBottom = mapToCoordinate(request.getLeftBottom());
        final GpsCoordinate rightUpper = mapToCoordinate(request.getRightUpper());
        return new AreaCoordinate(leftBottom, rightUpper);
    }

    private Geometry mapBounds(final SearchingCitiesProcess source) {
        return geoJSONWriter.write(source.getBounds());
    }

    private static GpsCoordinate mapToCoordinate(final CoordinateRequest request) {
        return new GpsCoordinate(request.getLatitude(), request.getLongitude());
    }
}
