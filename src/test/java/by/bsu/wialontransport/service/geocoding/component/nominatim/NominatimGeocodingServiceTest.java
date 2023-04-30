package by.bsu.wialontransport.service.geocoding.component.nominatim;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.service.geocoding.component.NominatimGeocodingService;
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
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.Optional;

import static by.bsu.wialontransport.util.GeometryUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static java.lang.String.format;
import static java.util.Optional.empty;
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

    @MockBean
    private AddressService mockedAddressService;

    @Value("${geocoding.url.template}")
    private String urlTemplate;

    @Autowired
    private NominatimGeocodingService nominatimGeocodingService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    @SuppressWarnings("unchecked")
    public void addressWithNotExistGeometryShouldBeReceived() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;

        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );
        final NominatimResponse givenResponse = NominatimResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new NominatimResponseAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .build();

        when(this.mockedRestTemplate.exchange(
                        eq(this.createUrl(givenLatitude, givenLongitude)),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(ok(givenResponse));

        when(this.mockedAddressService.findAddressByGeometry(givenGeometry))
                .thenReturn(empty());

        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();

        final Address expected = Address.builder()
                .boundingBox(createPolygon(this.geometryFactory, 3.3, 5.5, 3.3, 6.6, 4.4, 6.6, 4.4, 5.5))
                .center(createPoint(this.geometryFactory, 4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(givenGeometry)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addressWithExistGeometryShouldBeReceived() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;

        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );
        final NominatimResponse givenResponse = NominatimResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new NominatimResponseAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .build();

        when(this.mockedRestTemplate.exchange(
                        eq(this.createUrl(givenLatitude, givenLongitude)),
                        same(GET),
                        same(EMPTY),
                        any(ParameterizedTypeReference.class)
                )
        ).thenReturn(ok(givenResponse));

        final Address givenAlreadyExistAddress = Address.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 3.3, 5.5, 3.3, 6.6, 4.4, 6.6, 4.4, 5.5))
                .center(createPoint(this.geometryFactory, 4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(givenGeometry)
                .build();
        when(this.mockedAddressService.findAddressByGeometry(givenGeometry))
                .thenReturn(Optional.of(givenAlreadyExistAddress));

        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertEquals(givenAlreadyExistAddress, actual);
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
}
