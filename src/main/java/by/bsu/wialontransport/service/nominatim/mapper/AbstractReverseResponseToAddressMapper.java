package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.wololo.jts2geojson.GeoJSONReader;

@RequiredArgsConstructor
public abstract class AbstractReverseResponseToAddressMapper<T extends Address> {
    private static final int INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX = 0;
    private static final int INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX = 2;

    private static final int INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX = 1;
    private static final int INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX = 3;

    private final GeometryFactory geometryFactory;
    private final GeoJSONReader geoJSONReader;

    public final T map(final NominatimReverseResponse response) {
        final Geometry boundingBox = this.mapBoundingBox(response);
        final Point center = this.mapCenter(response);
        final NominatimReverseResponse.Address address = response.getAddress();
        final String cityName = address.getCityName();
        final String countryName = address.getCountryName();
        final Geometry geometry = this.mapGeometry(response);
        return this.createAddress(boundingBox, center, cityName, countryName, geometry);
    }

    protected abstract T createAddress(final Geometry boundingBox,
                                       final Point center,
                                       final String cityName,
                                       final String countryName,
                                       final Geometry geometry);

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
        return new CoordinateXY(
                boundingBoxCoordinates[INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX],
                boundingBoxCoordinates[INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX]
        );
    }

    private static CoordinateXY findLeftUpperCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return new CoordinateXY(
                boundingBoxCoordinates[INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX],
                boundingBoxCoordinates[INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX]
        );
    }

    private static CoordinateXY findRightUpperCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return new CoordinateXY(
                boundingBoxCoordinates[INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX],
                boundingBoxCoordinates[INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX]
        );
    }

    private static CoordinateXY findRightBottomCoordinateOfBoundingBox(final double[] boundingBoxCoordinates) {
        return new CoordinateXY(
                boundingBoxCoordinates[INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX],
                boundingBoxCoordinates[INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX]
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
                response.getCenterLatitude(),
                response.getCenterLongitude()
        );
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry mapGeometry(final NominatimReverseResponse response) {
        final org.wololo.geojson.Geometry mappedGeometry = response.getGeometry();
        return this.geoJSONReader.read(mappedGeometry);
    }
}
