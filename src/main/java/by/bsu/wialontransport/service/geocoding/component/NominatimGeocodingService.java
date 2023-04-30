package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.wololo.jts2geojson.GeoJSONReader;

import java.util.Optional;

import static java.util.Optional.empty;

//TODO: refactor tests for it
@Service
@Order(2)
public class NominatimGeocodingService implements GeocodingChainComponent {
    private final NominatimService nominatimService;
    private final ResponseToAddressMapper responseToAddressMapper;
    private final AddressService addressService;

    public NominatimGeocodingService(final NominatimService nominatimService,
                                     final AddressService addressService,
                                     final GeometryFactory geometryFactory,
                                     final GeoJSONReader geoJSONReader) {
        this.nominatimService = nominatimService;
        this.addressService = addressService;
        this.responseToAddressMapper = new ResponseToAddressMapper(geometryFactory, geoJSONReader);
    }

    @Override
    public Optional<Address> receive(final double latitude, final double longitude) {
        final NominatimReverseResponse response = this.nominatimService.reverse(latitude, longitude);
        return response != null
                ? Optional.of(this.mapToPossiblySavedAddress(response))
                : empty();
    }

    private Address mapToPossiblySavedAddress(final NominatimReverseResponse response) {
        final Address responseAddress = this.responseToAddressMapper.map(response);
        final Geometry responseAddressGeometry = responseAddress.getGeometry();
        final Optional<Address> optionalSavedAddressWithSameGeometry = this.addressService.findAddressByGeometry(
                responseAddressGeometry
        );
        return optionalSavedAddressWithSameGeometry.orElse(responseAddress);
    }

    @RequiredArgsConstructor
    private static final class ResponseToAddressMapper {
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
                    .cityName(address.getCityName())
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
}
