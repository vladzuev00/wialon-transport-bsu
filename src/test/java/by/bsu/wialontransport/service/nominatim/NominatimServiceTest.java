package by.bsu.wialontransport.service.nominatim;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.nominatim.exception.NominatimException;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

public final class NominatimServiceTest extends AbstractContextTest {

    @Value("${nominatim.reverse.url.template}")
    private String urlTemplate;

    @MockBean
    private RestTemplate mockedRestTemplate;

    @Autowired
    private NominatimService nominatimService;

    @Test
    @SuppressWarnings("unchecked")
    public void latitudeAndLongitudeShouldBeReversed() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 4.5;

        final String givenUrl = this.createUrl(givenLatitude, givenLongitude);
        final NominatimReverseResponse givenResponse = createReverseResponse();
        when(this.mockedRestTemplate.exchange(
                eq(givenUrl),
                same(GET),
                same(EMPTY),
                any(ParameterizedTypeReference.class))
        ).thenReturn(ok(givenResponse));

        final NominatimReverseResponse actual = this.nominatimService.reverse(givenLatitude, givenLongitude);
        assertSame(givenResponse, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void coordinateShouldBeReversed() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 4.5;
        final Coordinate givenCoordinate = new Coordinate(givenLatitude, givenLongitude);

        final String givenUrl = this.createUrl(givenLatitude, givenLongitude);
        final NominatimReverseResponse givenResponse = createReverseResponse();
        when(this.mockedRestTemplate.exchange(
                eq(givenUrl),
                same(GET),
                same(EMPTY),
                any(ParameterizedTypeReference.class))
        ).thenReturn(ok(givenResponse));

        final NominatimReverseResponse actual = this.nominatimService.reverse(givenCoordinate);
        assertSame(givenResponse, actual);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NominatimException.class)
    public void latitudeAndLongitudeShouldNotBeReversedBecauseOfBadHttpStatus() {
        final double givenLatitude = 6.5;
        final double givenLongitude = 5.5;

        final String givenUrl = this.createUrl(givenLatitude, givenLongitude);
        final ResponseEntity<?> givenResponseEntity = status(BAD_REQUEST).build();
        when(this.mockedRestTemplate.exchange(
                eq(givenUrl),
                same(GET),
                same(EMPTY),
                any(ParameterizedTypeReference.class))
        ).thenReturn(givenResponseEntity);

        this.nominatimService.reverse(givenLatitude, givenLongitude);
    }

    private String createUrl(final double latitude, final double longitude) {
        return format(this.urlTemplate, latitude, longitude);
    }

    private static NominatimReverseResponse createReverseResponse() {
        return NominatimReverseResponse.builder().build();
    }
}
