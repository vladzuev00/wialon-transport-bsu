package by.vladzuev.locationreceiver.service.geocoding;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.geocoding.geocoder.Geocoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class GeocodingServiceTest {

    @Mock
    private Geocoder firstMockedGeocoder;

    @Mock
    private Geocoder secondMockedGeocoder;

    @Mock
    private Geocoder thirdMockedGeocoder;

    @Mock
    private AddressService mockedAddressService;

    private GeocodingService geocodingService;

    @BeforeEach
    public void initializeGeocodingService() {
        geocodingService = new GeocodingService(
                List.of(firstMockedGeocoder, secondMockedGeocoder, thirdMockedGeocoder),
                mockedAddressService
        );
    }

    @Test
    public void newAddressShouldBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(firstMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());

        final Address givenAddress = mock(Address.class);
        when(secondMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        when(givenAddress.getId()).thenReturn(null);

        final Geometry givenGeometry = mock(Geometry.class);
        when(givenAddress.getGeometry()).thenReturn(givenGeometry);

        when(mockedAddressService.findByGeometry(same(givenGeometry))).thenReturn(empty());

        final Address givenSavedAddress = Address.builder().build();
        when(mockedAddressService.save(same(givenAddress))).thenReturn(givenSavedAddress);

        final Optional<Address> optionalActual = geocodingService.geocodeSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenSavedAddress, actual);

        verifyNoInteractions(thirdMockedGeocoder);
    }

    @Test
    public void newAddressWithExistingGeometryShouldBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(firstMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());

        final Address givenAddress = mock(Address.class);
        when(secondMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        when(givenAddress.getId()).thenReturn(null);

        final Geometry givenGeometry = mock(Geometry.class);
        when(givenAddress.getGeometry()).thenReturn(givenGeometry);

        final Address givenSavedAddress = Address.builder().build();
        when(mockedAddressService.findByGeometry(same(givenGeometry))).thenReturn(Optional.of(givenSavedAddress));

        final Optional<Address> optionalActual = geocodingService.geocodeSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenSavedAddress, actual);

        verifyNoInteractions(thirdMockedGeocoder);
        verify(mockedAddressService, times(0)).save(any(Address.class));
    }

    @Test
    public void existingAddressShouldBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(firstMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());

        final Address givenAddress = mock(Address.class);
        when(secondMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        final Long givenId = 255L;
        when(givenAddress.getId()).thenReturn(givenId);

        final Optional<Address> optionalActual = geocodingService.geocodeSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);

        verifyNoInteractions(thirdMockedGeocoder, mockedAddressService);
    }

    @Test
    public void addressShouldNotBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(firstMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());
        when(secondMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());
        when(thirdMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());

        final Optional<Address> optionalActual = geocodingService.geocodeSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(mockedAddressService);
    }
}
