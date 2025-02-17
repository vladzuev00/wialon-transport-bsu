package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.NominatimClient;
import by.vladzuev.locationreceiver.service.nominatim.mapper.ReverseResponseMapper;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class NominatimGeocoderTest {

    @Mock
    private NominatimClient mockedClient;

    @Mock
    private ReverseResponseMapper mockedMapper;

    private NominatimGeocoder geocoder;

    @BeforeEach
    public void initializeGeocoder() {
        geocoder = new NominatimGeocoder(mockedClient, mockedMapper);
    }

    @Test
    public void coordinateShouldBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        final NominatimReverseResponse givenResponse = NominatimReverseResponse.builder().build();
        when(mockedClient.reverse(same(givenCoordinate))).thenReturn(Optional.of(givenResponse));

        final Address givenAddress = Address.builder().build();
        when(mockedMapper.map(same(givenResponse))).thenReturn(givenAddress);

        final Optional<Address> optionalActual = geocoder.geocode(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);
    }

    @Test
    public void coordinateShouldNotBeGeocoded() {
        final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

        when(mockedClient.reverse(same(givenCoordinate))).thenReturn(Optional.empty());

        final Optional<Address> optionalActual = geocoder.geocode(givenCoordinate);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(mockedMapper);
    }
}
