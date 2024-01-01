//package by.bsu.wialontransport.service.geocoding.component;
//
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.crud.service.AddressService;
//import by.bsu.wialontransport.service.nominatim.NominatimService;
//import by.bsu.wialontransport.service.geocoding.service.nominatim.ReverseResponseToAddressMapper;
//import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.locationtech.jts.geom.Geometry;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Optional;
//
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class NominatimGeocodingServiceTest {
//
//    @Mock
//    private NominatimService mockedNominatimService;
//
//    @Mock
//    private ReverseResponseToAddressMapper mockedResponseToAddressMapper;
//
//    @Mock
//    private AddressService mockedAddressService;
//
//    private NominatimGeocodingService geocodingService;
//
//    @Before
//    public void initializeGeocodingService() {
//        this.geocodingService = new NominatimGeocodingService(
//                this.mockedNominatimService,
//                this.mockedResponseToAddressMapper,
//                this.mockedAddressService
//        );
//    }
//
//    @Test
//    public void addressWithNotExistGeometryShouldBeReceived() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        final NominatimReverseResponse givenReverseResponse = createNominatimReverseResponse();
//        when(this.mockedNominatimService.reverse(givenLatitude, givenLongitude))
//                .thenReturn(givenReverseResponse);
//
//        final Geometry givenAddressGeometry = mock(Geometry.class);
//        final Address givenAddress = createAddress(givenAddressGeometry);
//        when(this.mockedResponseToAddressMapper.map(same(givenReverseResponse)))
//                .thenReturn(givenAddress);
//
//        when(this.mockedAddressService.findByGeometry(same(givenAddressGeometry))).thenReturn(empty());
//
//        final Optional<Address> optionalActual = this.geocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isPresent());
//        final Address actual = optionalActual.get();
//        assertSame(givenAddress, actual);
//    }
//
//    @Test
//    public void addressWithExistGeometryShouldBeReceived() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        final NominatimReverseResponse givenReverseResponse = createNominatimReverseResponse();
//        when(this.mockedNominatimService.reverse(givenLatitude, givenLongitude))
//                .thenReturn(givenReverseResponse);
//
//        final Geometry givenAddressGeometry = mock(Geometry.class);
//        final Address givenAddress = createAddress(givenAddressGeometry);
//        when(this.mockedResponseToAddressMapper.map(same(givenReverseResponse)))
//                .thenReturn(givenAddress);
//
//        final Address givenAlreadySavedAddressWithSameGeometry = createAddress(givenAddressGeometry);
//        when(this.mockedAddressService.findByGeometry(same(givenAddressGeometry)))
//                .thenReturn(Optional.of(givenAlreadySavedAddressWithSameGeometry));
//
//        final Optional<Address> optionalActual = this.geocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isPresent());
//        final Address actual = optionalActual.get();
//        assertSame(givenAlreadySavedAddressWithSameGeometry, actual);
//    }
//
//    @Test
//    public void addressShouldNotBeReceived() {
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//
//        when(this.mockedNominatimService.reverse(givenLatitude, givenLongitude)).thenReturn(null);
//
//        final Optional<Address> optionalActual = this.geocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    private static NominatimReverseResponse createNominatimReverseResponse() {
//        return NominatimReverseResponse.builder().build();
//    }
//
//    private static Address createAddress(final Geometry geometry) {
//        return Address.builder()
//                .geometry(geometry)
//                .build();
//    }
//}
