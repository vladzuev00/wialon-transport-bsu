package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

@Component
@RequiredArgsConstructor
public final class ReverseResponseMapper {
    private static final String NOT_DEFINED_CITY_NAME = "not defined";

    private static final int INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX = 0;
    private static final int INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX = 2;

    private static final int INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX = 1;
    private static final int INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX = 3;

    private final GeometryFactory geometryFactory;
    private final GeoJSONReader geoJSONReader;

    public Address map(final NominatimReverseResponse response) {
        final NominatimReverseResponse.Address address = response.getAddress();
        return Address.builder()
                .boundingBox(this.mapBoundingBox(response))
                .center(this.mapCenter(response))
                .cityName(mapCityName(response))
                .countryName(address.getCountryName())
                .geometry(this.mapGeometry(response))
                .build();
    }

    private Geometry mapBoundingBox(final NominatimReverseResponse response) {
        final double[] boundingBoxCoordinates = response.getBoundingBoxCoordinates();
        return this.createBoundingBox(
                findLeftBottomCoordinateOfBoundingBox(boundingBoxCoordinates),
                findLeftUpperCoordinateOfBoundingBox(boundingBoxCoordinates),
                findRightUpperCoordinateOfBoundingBox(boundingBoxCoordinates),
                findRightBottomCoordinateOfBoundingBox(boundingBoxCoordinates)
        );
    }

    private static CoordinateXY findLeftBottomCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return findCoordinateOfBoundingBox(
                boundingBoxCoordinates,
                INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX,
                INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX
        );
    }

    private static CoordinateXY findLeftUpperCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return findCoordinateOfBoundingBox(
                boundingBoxCoordinates,
                INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX,
                INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX
        );
    }

    private static CoordinateXY findRightUpperCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return findCoordinateOfBoundingBox(
                boundingBoxCoordinates,
                INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX,
                INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX
        );
    }

    private static CoordinateXY findRightBottomCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return findCoordinateOfBoundingBox(
                boundingBoxCoordinates,
                INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX,
                INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX
        );
    }

    private static CoordinateXY findCoordinateOfBoundingBox(final double[] boundingBoxCoordinates,
                                                            final int latitudeIndex,
                                                            final int longitudeIndex) {
        return new CoordinateXY(
                boundingBoxCoordinates[longitudeIndex],
                boundingBoxCoordinates[latitudeIndex]
        );
    }

    private Geometry createBoundingBox(final CoordinateXY leftBottomCoordinate,
                                       final CoordinateXY leftUpperCoordinate,
                                       final CoordinateXY rightUpperCoordinate,
                                       final CoordinateXY rightBottomCoordinate) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                leftBottomCoordinate,
                leftUpperCoordinate,
                rightUpperCoordinate,
                rightBottomCoordinate,
                leftBottomCoordinate
        });
    }

    private Point mapCenter(final NominatimReverseResponse response) {
        final CoordinateXY coordinate = new CoordinateXY(
                response.getCenterLongitude(),
                response.getCenterLatitude()
        );
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry mapGeometry(final NominatimReverseResponse response) {
        final org.wololo.geojson.Geometry mappedGeometry = response.getGeometry();
        return this.geoJSONReader.read(mappedGeometry, this.geometryFactory);
    }

    private static String mapCityName(final NominatimReverseResponse response) {
        final NominatimReverseResponse.Address address = response.getAddress();
        final String responseCityName = address.getCityName();
        return responseCityName != null ? responseCityName : NOT_DEFINED_CITY_NAME;
    }
}
