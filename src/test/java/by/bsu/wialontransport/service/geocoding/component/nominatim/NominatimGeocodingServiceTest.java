package by.bsu.wialontransport.service.geocoding.component.nominatim;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.geocoding.component.nominatim.dto.NominatimResponse;
import by.bsu.wialontransport.service.geocoding.component.nominatim.dto.NominatimResponse.NominatimResponseAddress;
import by.bsu.wialontransport.service.geocoding.exception.GeocodingException;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.ok;

public final class NominatimGeocodingServiceTest extends AbstractContextTest {

    @MockBean
    private RestTemplate mockedRestTemplate;

    @Value("${geocoding.url.format}")
    private String urlTemplate;

    @Autowired
    private NominatimGeocodingService nominatimGeocodingService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @SuppressWarnings("unchecked")
    public void addressShouldBeReceived() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;

        final NominatimResponse givenResponse = NominatimResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new NominatimResponseAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .build();
        when(this.mockedRestTemplate.exchange(
                        eq(this.createUrl(givenLatitude, givenLongitude)),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(ok(givenResponse));

        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();

        final Address expected = Address.builder()
                .boundingBox(this.createPolygon(3.3, 5.5, 3.3, 6.6, 4.4, 6.6, 4.4, 5.5))
                .center(this.createPoint(4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .build();

        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addressShouldNotBeReceived() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;

        when(this.mockedRestTemplate.exchange(
                        eq(this.createUrl(givenLatitude, givenLongitude)),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(ok(null));

        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = GeocodingException.class)
    public void addressShouldNotBeReceivedBecauseOfBadHttpStatus() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;

        when(this.mockedRestTemplate.exchange(
                        eq(this.createUrl(givenLatitude, givenLongitude)),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(new ResponseEntity<>(BAD_REQUEST));

        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isEmpty());
    }

    private String createUrl(final double latitude, final double longitude) {
        return format(this.urlTemplate, latitude, longitude);
    }

    private Point createPoint(final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry createPolygon(final double firstX, final double firstY,
                                   final double secondX, final double secondY,
                                   final double thirdX, final double thirdY,
                                   final double fourthX, final double fourthY) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                        new CoordinateXY(firstX, firstY),
                        new CoordinateXY(secondX, secondY),
                        new CoordinateXY(thirdX, thirdY),
                        new CoordinateXY(fourthX, fourthY),
                        new CoordinateXY(firstX, firstY)
                }
        );
    }
}
