package by.vladzuev.locationreceiver.service.nominatim;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Component
public class NominatimService {
    private static final String TEMPLATE_REQUESTING_MESSAGE = "Request to Nominatim: {}";
    private static final ParameterizedTypeReference<NominatimReverseResponse> PARAMETERIZED_TYPE_REFERENCE_REVERSE
            = new ParameterizedTypeReference<>() {
    };

    private final String reverseUrlTemplate;
    private final RestTemplate restTemplate;

    public NominatimService(@Value("${nominatim.reverse.url.template}") final String reverseUrlTemplate,
                            final RestTemplate restTemplate) {
        this.reverseUrlTemplate = reverseUrlTemplate;
        this.restTemplate = restTemplate;
    }

    public Optional<NominatimReverseResponse> reverse(final GpsCoordinate coordinate) {
        final String url = createReverseUrl(coordinate);
        log.info(TEMPLATE_REQUESTING_MESSAGE, url);
        final ResponseEntity<NominatimReverseResponse> responseEntity = restTemplate.exchange(
                url,
                GET,
                EMPTY,
                PARAMETERIZED_TYPE_REFERENCE_REVERSE
        );
        validateResponseEntity(responseEntity, url);
        return ofNullable(responseEntity.getBody());
    }

    private String createReverseUrl(final GpsCoordinate coordinate) {
        return format(reverseUrlTemplate, coordinate.getLatitude(), coordinate.getLongitude());
    }

    private static void validateResponseEntity(final ResponseEntity<?> responseEntity, final String url) {
//        final HttpStatus httpStatus = responseEntity.getStatusCode();
//        if (httpStatus != OK) {
//            throw new NominatimException(
//                    format("Http status after doing request to '%s' is '%s'", url, httpStatus)
//            );
//        }
    }

    static final class NominatimException extends RuntimeException {

        @SuppressWarnings("unused")
        public NominatimException() {

        }

        public NominatimException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NominatimException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NominatimException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
