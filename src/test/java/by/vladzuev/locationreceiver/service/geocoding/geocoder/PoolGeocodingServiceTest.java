package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class PoolGeocodingServiceTest {

    @Mock
    private AddressService mockedAddressService;

    private PoolGeocodingService geocodingService;

    @Before
    public void initializeGeocodingService() {
        geocodingService = new PoolGeocodingService(mockedAddressService);
    }

    @Test
    public void addressShouldBeReceived() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        final Address givenAddress = createAddress(255L);
        when(mockedAddressService.findByGpsCoordinates(givenCoordinate)).thenReturn(Optional.of(givenAddress));

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);
    }

    @Test
    public void addressShouldNotBeReceived() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        when(mockedAddressService.findByGpsCoordinates(givenCoordinate)).thenReturn(empty());

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }
}
