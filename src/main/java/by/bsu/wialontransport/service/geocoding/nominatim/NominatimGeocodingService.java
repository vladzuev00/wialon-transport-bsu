package by.bsu.wialontransport.service.geocoding.nominatim;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.geocoding.GeocodingService;
import by.bsu.wialontransport.service.geocoding.exception.GeocodingException;
import by.bsu.wialontransport.service.geocoding.nominatim.dto.NominatimResponse;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    private static final ParameterizedTypeReference<NominatimResponse> PARAMETERIZED_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final String urlTemplate;
    private final RestTemplate restTemplate;

    public NominatimGeocodingService(@Value("${geocoding.url.format}") final String urlTemplate,
                                     final RestTemplate restTemplate) {
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
                url, GET, EMPTY, PARAMETERIZED_TYPE_REFERENCE
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

    private static final class ResponseToAddressMapper {
        private final GeometryFactory geometryFactory;

        public Address map(final NominatimResponse response) {

        }

        private Geometry mapBoundingBox(final NominatimResponse response) {
            return
        }
    }
}
