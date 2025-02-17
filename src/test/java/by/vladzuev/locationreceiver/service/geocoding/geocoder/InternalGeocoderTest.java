package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class InternalGeocoderTest {

    @Mock
    private AddressService mockedAddressService;

    private InternalGeocoder geocoder;

    @BeforeEach
    public void initializeGeocoder() {
        geocoder = new InternalGeocoder(mockedAddressService);
    }

    @Test
    public void addressShouldBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        final Address givenAddress = Address.builder().build();
        when(mockedAddressService.findByCoordinate(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        final Optional<Address> optionalActual = geocoder.geocode(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);
    }

    @Test
    public void addressShouldNotBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(mockedAddressService.findByCoordinate(givenCoordinate)).thenReturn(empty());

        final Optional<Address> optionalActual = geocoder.geocode(givenCoordinate);
        assertTrue(optionalActual.isEmpty());
    }
}
