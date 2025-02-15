package by.vladzuev.locationreceiver.service.nominatim;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

public final class NominatimServiceTest extends AbstractSpringBootTest {

    @Value("${nominatim.reverse.url.template}")
    private String reverseUrlTemplate;

    @MockBean
    private RestTemplate mockedRestTemplate;

    @Autowired
    private NominatimService nominatimService;

    @Test
    @SuppressWarnings("unchecked")
    public void coordinateShouldBeReversed() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 6.6);

        final String givenUrl = createReverseUrl(givenCoordinate);
        final NominatimReverseResponse givenResponse = createReverseResponse();
        when(
                mockedRestTemplate.exchange(
                        eq(givenUrl),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(ok(givenResponse));

        final Optional<NominatimReverseResponse> optionalActual = nominatimService.reverse(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final NominatimReverseResponse actual = optionalActual.get();
        assertSame(givenResponse, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void coordinateShouldNotBeReversed() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 5.5);

        final String givenUrl = createReverseUrl(givenCoordinate);
        final ResponseEntity<?> givenResponseEntity = ok().build();
        when(
                mockedRestTemplate.exchange(
                        eq(givenUrl),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(givenResponseEntity);

        nominatimService.reverse(givenCoordinate);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NominatimService.NominatimException.class)
    public void coordinateShouldNotBeReversedBecauseOfBadHttpStatus() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 5.5);

        final String givenUrl = createReverseUrl(givenCoordinate);
        final ResponseEntity<?> givenResponseEntity = status(BAD_REQUEST).build();
        when(
                mockedRestTemplate.exchange(
                        eq(givenUrl),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(givenResponseEntity);

        nominatimService.reverse(givenCoordinate);
    }

    private String createReverseUrl(final GpsCoordinate coordinate) {
        return format(reverseUrlTemplate, coordinate.getLatitude(), coordinate.getLongitude());
    }

    private static NominatimReverseResponse createReverseResponse() {
        return NominatimReverseResponse.builder().build();
    }
}
