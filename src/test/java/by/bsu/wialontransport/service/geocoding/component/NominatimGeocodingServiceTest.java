package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.crud.service.AddressService;

import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToAddressMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.ResponseEntity.ok;

@RunWith(MockitoJUnitRunner.class)
public final class NominatimGeocodingServiceTest {

    @Mock
    private NominatimService mockedNominatimService;

    @Mock
    private ReverseResponseToAddressMapper mockedResponseToAddressMapper;

    @Mock
    private AddressService mockedAddressService;

    private NominatimGeocodingService geocodingService;

    @Before
    public void initializeGeocodingService() {
        this.geocodingService = new NominatimGeocodingService(
                this.mockedNominatimService,
                this.mockedResponseToAddressMapper,
                this.mockedAddressService
        );
    }

    @Test
    public void addressWithNotExistGeometryShouldBeReceived() {
        final NominatimReverseResponse givenReverseResponse = createNominatimReverseResponse();


        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;

        throw new RuntimeException();
    }

    private static NominatimReverseResponse createNominatimReverseResponse() {
        return NominatimReverseResponse.builder().build();
    }

//    @Test
//    @SuppressWarnings("unchecked")
//    public void addressWithNotExistGeometryShouldBeReceived() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        final Geometry givenGeometry = createPolygon(
//                this.geometryFactory,
//                1, 2, 3, 4, 5, 6
//        );
//        final NominatimResponse givenResponse = NominatimResponse.builder()
//                .centerLatitude(4.4)
//                .centerLongitude(5.5)
//                .address(new NominatimResponseAddress("city", "country"))
//                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
//                .geometry(this.geoJSONWriter.write(givenGeometry))
//                .build();
//
//        when(this.mockedRestTemplate.exchange(
//                        eq(this.createUrl(givenLatitude, givenLongitude)),
//                        same(GET),
//                        same(EMPTY),
//                        any(ParameterizedTypeReference.class)
//                )
//        ).thenReturn(ok(givenResponse));
//
//        when(this.mockedAddressService.findAddressByGeometry(givenGeometry))
//                .thenReturn(empty());
//
//        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
//                givenLatitude, givenLongitude
//        );
//        assertTrue(optionalActual.isPresent());
//        final Address actual = optionalActual.get();
//
//        final Address expected = Address.builder()
//                .boundingBox(createPolygon(this.geometryFactory, 3.3, 5.5, 3.3, 6.6, 4.4, 6.6, 4.4, 5.5))
//                .center(createPoint(this.geometryFactory, 4.4, 5.5))
//                .cityName("city")
//                .countryName("country")
//                .geometry(givenGeometry)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void addressWithExistGeometryShouldBeReceived() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        final Geometry givenGeometry = createPolygon(
//                this.geometryFactory,
//                1, 2, 3, 4, 5, 6
//        );
//        final NominatimResponse givenResponse = NominatimResponse.builder()
//                .centerLatitude(4.4)
//                .centerLongitude(5.5)
//                .address(new NominatimResponseAddress("city", "country"))
//                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
//                .geometry(this.geoJSONWriter.write(givenGeometry))
//                .build();
//
//        when(this.mockedRestTemplate.exchange(
//                        eq(this.createUrl(givenLatitude, givenLongitude)),
//                        same(GET),
//                        same(EMPTY),
//                        any(ParameterizedTypeReference.class)
//                )
//        ).thenReturn(ok(givenResponse));
//
//        final Address givenAlreadyExistAddress = Address.builder()
//                .id(255L)
//                .boundingBox(createPolygon(this.geometryFactory, 3.3, 5.5, 3.3, 6.6, 4.4, 6.6, 4.4, 5.5))
//                .center(createPoint(this.geometryFactory, 4.4, 5.5))
//                .cityName("city")
//                .countryName("country")
//                .geometry(givenGeometry)
//                .build();
//        when(this.mockedAddressService.findAddressByGeometry(givenGeometry))
//                .thenReturn(Optional.of(givenAlreadyExistAddress));
//
//        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
//                givenLatitude, givenLongitude
//        );
//        assertTrue(optionalActual.isPresent());
//        final Address actual = optionalActual.get();
//        assertEquals(givenAlreadyExistAddress, actual);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void addressShouldNotBeReceived() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        when(this.mockedRestTemplate.exchange(
//                        eq(this.createUrl(givenLatitude, givenLongitude)),
//                        same(GET),
//                        same(EMPTY),
//                        any(ParameterizedTypeReference.class)
//                )
//        ).thenReturn(ok(null));
//
//        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
//                givenLatitude, givenLongitude
//        );
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @SuppressWarnings("unchecked")
//    @Test(expected = GeocodingException.class)
//    public void addressShouldNotBeReceivedBecauseOfBadHttpStatus() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        when(this.mockedRestTemplate.exchange(
//                        eq(this.createUrl(givenLatitude, givenLongitude)),
//                        same(GET),
//                        same(EMPTY),
//                        any(ParameterizedTypeReference.class)
//                )
//        ).thenReturn(new ResponseEntity<>(BAD_REQUEST));
//
//        final Optional<Address> optionalActual = this.nominatimGeocodingService.receive(
//                givenLatitude, givenLongitude
//        );
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    private String createUrl(final double latitude, final double longitude) {
//        return format(this.urlTemplate, latitude, longitude);
//    }
}
