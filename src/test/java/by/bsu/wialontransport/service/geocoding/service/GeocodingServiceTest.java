package by.bsu.wialontransport.service.geocoding.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.model.GpsCoordinate;
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
