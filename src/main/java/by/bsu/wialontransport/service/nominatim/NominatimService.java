package by.bsu.wialontransport.service.nominatim;

import by.bsu.wialontransport.service.nominatim.exception.NominatimException;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Component
public class NominatimService {
    private final String urlTemplate;
    private final RestTemplate restTemplate;

    public NominatimService(@Value("${nominatim.reverse.url.template}") final String urlTemplate,
                            final RestTemplate restTemplate) {
        this.urlTemplate = urlTemplate;
        this.restTemplate = restTemplate;
    }

    public NominatimReverseResponse reverse(final double latitude, final double longitude) {
        final String url = this.createUrl(latitude, longitude);
        final ResponseEntity<NominatimReverseResponse> responseEntity = this.restTemplate.exchange(
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
            throw new NominatimException(
                    format("Http status after doing request to '%s' is '%s'.", url, httpStatus)
            );
        }
    }
}