package by.bsu.wialontransport.service.geocoding.nominatim;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.geocoding.GeocodingService;
import by.bsu.wialontransport.service.geocoding.exception.GeocodingException;
import by.bsu.wialontransport.service.geocoding.nominatim.dto.NominatimResponse;
import by.bsu.wialontransport.service.geocoding.nominatim.dto.NominatimResponse.NominatimResponseAddress;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Service
public final class NominatimGeocodingService implements GeocodingService {
    private final String urlTemplate;
    private final RestTemplate restTemplate;

    public NominatimGeocodingService(@Value("${geocoding.url.format}") final String urlTemplate,
                                     final RestTemplate restTemplate,
                                     final GeometryFactory geometryFactory) {
        this.urlTemplate = urlTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Address> receive(final double latitude, final double longitude) {
        final NominatimResponse response = this.doRequest(latitude, longitude);

    }

    private NominatimResponse doRequest(final double latitude, final double longitude) {
        final String url = this.createUrl(latitude, longitude);
        final ResponseEntity<NominatimResponse> responseEntity = this.restTemplate.exchange(
                url, GET, EMPTY, new ParameterizedTypeReference<>() {
                }
        );
        validateResponseEntity(responseEntity, url);
        return responseEntity.getBody();
    }

    private String createUrl(final double latitude, final double longitude) {
        return format(this.urlTemplate, latitude, longitude);
    }

    private static void validateResponseEntity(final ResponseEntity<?> responseEntity, final String url) {
        final HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus != OK) {
            throw new GeocodingException(
                    format("Http status after receiving address from '%s' is '%s'.", url, httpStatus)
            );
        }
    }

    @RequiredArgsConstructor
    private static final class ResponseToAddressMapper {
        private static final int INDEX_LEFT_BOTTOM_LATITUDE_OF_BOUNDING_BOX = 0;
        private static final int INDEX_LEFT_BOTTOM_LONGITUDE_OF_BOUNDING_BOX = 2;

        private static final int INDEX_RIGHT_UPPER_LATITUDE_OF_BOUNDING_BOX = 1;
        private static final int INDEX_RIGHT_UPPER_LONGITUDE_OF_BOUNDING_BOX = 3;

        private final GeometryFactory geometryFactory;

        public Address map(final NominatimResponse response) {
            final NominatimResponseAddress address = response.getAddress();
            return Address.builder()
                    .boundingBox(this.mapBoundingBox(response))
                    .centerLatitude(response.getCenterLatitude())
                    .centerLongitude(response.getCenterLongitude())
                    .cityName(address.getCityName())
                    .countryName(address.getCountryName())
                    .build();
        }

        private Geometry mapBoundingBox(final NominatimResponse response) {
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
    }
}
