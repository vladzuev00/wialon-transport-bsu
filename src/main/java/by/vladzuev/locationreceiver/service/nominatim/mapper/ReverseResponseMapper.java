package by.vladzuev.locationreceiver.service.nominatim.mapper;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

@Component
@RequiredArgsConstructor
public final class ReverseResponseMapper {
    private static final String NOT_DEFINED_CITY_NAME = "not defined";

    private static final int BOUNDING_BOX_LEFT_BOTTOM_LATITUDE_INDEX = 0;
    private static final int BOUNDING_BOX_LEFT_BOTTOM_LONGITUDE_INDEX = 2;

    private static final int BOUNDING_BOX_RIGHT_UPPER_LATITUDE_INDEX = 1;
    private static final int BOUNDING_BOX_RIGHT_UPPER_LONGITUDE_INDEX = 3;

    private final GeometryFactory geometryFactory;
    private final GeoJSONReader geoJSONReader;

    public Address map(final NominatimReverseResponse response) {
        return Address.builder()
                .boundingBox(mapBoundingBox(response))
                .center(mapCenter(response))
                .cityName(mapCityName(response))
                .countryName(mapCountryName(response))
                .geometry(mapGeometry(response))
                .build();
    }

    private Geometry mapBoundingBox(final NominatimReverseResponse response) {
        final double[] boundingBoxCoordinates = response.getBoundingBoxCoordinates();
        return createBoundingBox(
                findBoundingBoxLeftBottomCoordinate(boundingBoxCoordinates),
                findBoundingBoxLeftUpperCoordinate(boundingBoxCoordinates),
                findBoundingBoxRightUpperCoordinate(boundingBoxCoordinates),
                findBoundingBoxRightBottomCoordinate(boundingBoxCoordinates)
        );
    }

    private static CoordinateXY findBoundingBoxLeftBottomCoordinate(final double[] boundingBoxCoordinates) {
        return findBoundingBoxCoordinate(
                boundingBoxCoordinates,
                BOUNDING_BOX_LEFT_BOTTOM_LATITUDE_INDEX,
                BOUNDING_BOX_LEFT_BOTTOM_LONGITUDE_INDEX
        );
    }

    private static CoordinateXY findBoundingBoxLeftUpperCoordinate(final double[] boundingBoxCoordinates) {
        return findBoundingBoxCoordinate(
                boundingBoxCoordinates,
                BOUNDING_BOX_LEFT_BOTTOM_LATITUDE_INDEX,
                BOUNDING_BOX_RIGHT_UPPER_LONGITUDE_INDEX
        );
    }

    private static CoordinateXY findBoundingBoxRightUpperCoordinate(final double[] boundingBoxCoordinates) {
        return findBoundingBoxCoordinate(
                boundingBoxCoordinates,
                BOUNDING_BOX_RIGHT_UPPER_LATITUDE_INDEX,
                BOUNDING_BOX_RIGHT_UPPER_LONGITUDE_INDEX
        );
    }

    private static CoordinateXY findBoundingBoxRightBottomCoordinate(final double[] boundingBoxCoordinates) {
        return findBoundingBoxCoordinate(
                boundingBoxCoordinates,
                BOUNDING_BOX_RIGHT_UPPER_LATITUDE_INDEX,
                BOUNDING_BOX_LEFT_BOTTOM_LONGITUDE_INDEX
        );
    }

    private static CoordinateXY findBoundingBoxCoordinate(final double[] boundingBoxCoordinates,
                                                          final int latitudeIndex,
                                                          final int longitudeIndex) {
        return new CoordinateXY(boundingBoxCoordinates[longitudeIndex], boundingBoxCoordinates[latitudeIndex]);
    }

    private Geometry createBoundingBox(final CoordinateXY leftBottomCoordinate,
                                       final CoordinateXY leftUpperCoordinate,
                                       final CoordinateXY rightUpperCoordinate,
                                       final CoordinateXY rightBottomCoordinate) {
        final Coordinate[] coordinates = new Coordinate[]{
                leftBottomCoordinate,
                leftUpperCoordinate,
                rightUpperCoordinate,
                rightBottomCoordinate,
                leftBottomCoordinate
        };
        return geometryFactory.createPolygon(coordinates);
    }

    private Point mapCenter(final NominatimReverseResponse response) {
        final CoordinateXY coordinate = new CoordinateXY(response.getCenterLongitude(), response.getCenterLatitude());
        return geometryFactory.createPoint(coordinate);
    }

    private Geometry mapGeometry(final NominatimReverseResponse response) {
        return geoJSONReader.read(response.getGeometry(), geometryFactory);
    }

    private static String mapCityName(final NominatimReverseResponse response) {
        final String cityName = findCityName(response);
        return cityName != null ? cityName : NOT_DEFINED_CITY_NAME;
    }

    private static String mapCountryName(final NominatimReverseResponse response) {
        return response.getAddress().getCountryName();
    }

    private static String findCityName(final NominatimReverseResponse response) {
        return response.getAddress().getCityName();
    }
}
