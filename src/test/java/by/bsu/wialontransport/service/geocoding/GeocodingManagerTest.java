package by.bsu.wialontransport.service.geocoding;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.service.geocoding.service.GeocodingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class GeocodingManagerTest {

    @Mock
    private GeocodingService firstMockedGeocodingService;

    @Mock
    private GeocodingService secondMockedGeocodingService;

    @Mock
    private AddressService mockedAddressService;

    private GeocodingManager geocodingManager;

    @Before
    public void initializeGeocodingManager() {
        geocodingManager = new GeocodingManager(
                List.of(firstMockedGeocodingService, secondMockedGeocodingService),
                mockedAddressService
        );
    }

    @Test
    public void alreadyExistingAddressShouldBeFound() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        final Address givenAddress = createAddress(255L);
        when(firstMockedGeocodingService.receive(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        final Optional<Address> optionalActual = geocodingManager.findSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);

        verifyNoInteractions(secondMockedGeocodingService);
        verifyNoInteractions(mockedAddressService);
    }

    @Test
    public void newAddressShouldBeFound() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        when(firstMockedGeocodingService.receive(same(givenCoordinate))).thenReturn(empty());

        final Address givenAddress = createAddress();
        when(secondMockedGeocodingService.receive(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        final Address givenSavedAddress = createAddress(256L);
        when(mockedAddressService.save(same(givenAddress))).thenReturn(givenSavedAddress);

        final Optional<Address> optionalActual = geocodingManager.findSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenSavedAddress, actual);

        verify(firstMockedGeocodingService, times(1)).receive(same(givenCoordinate));
    }

    @Test
    public void addressShouldNotBeFound() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        when(firstMockedGeocodingService.receive(same(givenCoordinate))).thenReturn(empty());
        when(secondMockedGeocodingService.receive(same(givenCoordinate))).thenReturn(empty());

        final Optional<Address> optionalActual = geocodingManager.findSavedAddress(givenCoordinate);
        assertTrue(optionalActual.isEmpty());

        verify(firstMockedGeocodingService, times(1)).receive(same(givenCoordinate));
        verify(secondMockedGeocodingService, times(1)).receive(same(givenCoordinate));
        verifyNoInteractions(mockedAddressService);
    }

    private static Address createAddress() {
        return Address.builder().build();
    }

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }
}
