package by.vladzuev.locationreceiver.service.geocoding;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.geocoding.geocoder.Geocoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private GeocodingService geocodingService;

    @BeforeEach
    public void initializeGeocodingService() {
        geocodingService = new GeocodingService(List.of(firstMockedGeocoder, secondMockedGeocoder, thirdMockedGeocoder));
    }

    @Test
    public void coordinateShouldBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(firstMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());

        final Address givenAddress = mock(Address.class);
        when(secondMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);

        verifyNoInteractions(thirdMockedGeocoder);
    }

    @Test
    public void coordinateShouldNotBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(firstMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());
        when(secondMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());
        when(thirdMockedGeocoder.geocode(same(givenCoordinate))).thenReturn(empty());

        final Optional<Address> optionalActual = geocodingService.geocode(givenCoordinate);
        assertTrue(optionalActual.isEmpty());
    }
}
