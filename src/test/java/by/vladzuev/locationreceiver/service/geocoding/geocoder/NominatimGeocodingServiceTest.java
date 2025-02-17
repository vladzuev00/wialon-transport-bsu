package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.NominatimService;
import by.vladzuev.locationreceiver.service.nominatim.mapper.ReverseResponseMapper;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class NominatimGeocodingServiceTest {

    @Mock
    private NominatimService mockedNominatimService;

    @Mock
    private ReverseResponseMapper mockedResponseMapper;

    @Mock
    private AddressService mockedAddressService;

    private NominatimGeocodingService geocodingService;

    @Before
    public void initializeGeocodingService() {
        geocodingService = new NominatimGeocodingService(
                mockedNominatimService,
                mockedResponseMapper,
                mockedAddressService
        );
    }

    @Test
    public void alreadySavedAddressShouldBeReceived() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        final NominatimReverseResponse givenResponse = createResponse();
        when(mockedNominatimService.reverse(same(givenCoordinate))).thenReturn(Optional.of(givenResponse));

        final Geometry givenGeometry = mock(Geometry.class);
        final Address givenAddress = createAddress(givenGeometry);
        when(mockedResponseMapper.map(same(givenResponse))).thenReturn(givenAddress);

        final Address givenSavedAddress = createAddress(255L, givenGeometry);
        when(mockedAddressService.findByGeometry(same(givenGeometry))).thenReturn(Optional.of(givenSavedAddress));

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenSavedAddress, actual);
    }

    @Test
    public void newAddressShouldBeFound() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        final NominatimReverseResponse givenResponse = createResponse();
        when(mockedNominatimService.reverse(same(givenCoordinate))).thenReturn(Optional.of(givenResponse));

        final Geometry givenGeometry = mock(Geometry.class);
        final Address givenAddress = createAddress(givenGeometry);
        when(mockedResponseMapper.map(same(givenResponse))).thenReturn(givenAddress);

        when(mockedAddressService.findByGeometry(same(givenGeometry))).thenReturn(empty());

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);
    }

    @Test
    public void addressShouldNotBeFound() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        when(mockedNominatimService.reverse(same(givenCoordinate))).thenReturn(empty());

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(mockedResponseMapper);
        verifyNoInteractions(mockedAddressService);
    }

    private static NominatimReverseResponse createResponse() {
        return NominatimReverseResponse.builder().build();
    }

    private static Address createAddress(final Geometry geometry) {
        return Address.builder()
                .geometry(geometry)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Address createAddress(final Long id, final Geometry geometry) {
        return Address.builder()
                .id(id)
                .geometry(geometry)
                .build();
    }
}
