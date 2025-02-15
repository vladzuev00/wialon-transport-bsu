package by.vladzuev.locationreceiver.service.geocoding.service;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class GeocodingServiceTest {
    private final GeocodingService geocodingService = new TestGeocodingService();

    @Test
    public void nameShouldBeFound() {
        final String actual = geocodingService.findName();
        final String expected = "TestGeocodingService";
        assertEquals(expected, actual);
    }

    private static final class TestGeocodingService implements GeocodingService {

        @Override
        public Optional<Address> receive(final GpsCoordinate coordinate) {
            throw new UnsupportedOperationException();
        }
    }
}
