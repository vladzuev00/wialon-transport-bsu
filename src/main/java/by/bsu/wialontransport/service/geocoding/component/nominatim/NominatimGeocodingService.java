package by.bsu.wialontransport.service.geocoding.component.nominatim;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.geocoding.component.GeocodingChainComponent;
import by.bsu.wialontransport.service.geocoding.exception.GeocodingException;
import by.bsu.wialontransport.service.geocoding.component.nominatim.dto.NominatimResponse;
import by.bsu.wialontransport.service.geocoding.component.nominatim.dto.NominatimResponse.NominatimResponseAddress;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Service
@Order(2)
public final class NominatimGeocodingService implements GeocodingChainComponent {
    private final String urlTemplate;
    private final RestTemplate restTemplate;
    private final ResponseToAddressMapper responseToAddressMapper;

    public NominatimGeocodingService(@Value("${geocoding.url.format}") final String urlTemplate,
                                     final RestTemplate restTemplate,
                                     final GeometryFactory geometryFactory) {
        this.urlTemplate = urlTemplate;
        this.restTemplate = restTemplate;
        this.responseToAddressMapper = new ResponseToAddressMapper(geometryFactory);
    }

    @Override
    public Optional<Address> receive(final double latitude, final double longitude) {
        final NominatimResponse response = this.doRequest(latitude, longitude);
        return response != null
                ? Optional.of(this.responseToAddressMapper.map(response))
                : empty();
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
                    .center(this.mapCenter(response))
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

        private Point mapCenter(final NominatimResponse response) {
            final CoordinateXY coordinate = new CoordinateXY(
                    response.getCenterLatitude(),
                    response.getCenterLongitude()
            );
            return this.geometryFactory.createPoint(coordinate);
        }
    }
}