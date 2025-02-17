package by.vladzuev.locationreceiver.service.geocoding.aspect;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.geocoding.geocoder.Geocoder;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({GeocodingLoggingAspectTest.SuccessGeocoder.class, GeocodingLoggingAspectTest.FailureGeocoder.class})
public final class GeocodingLoggingAspectTest extends AbstractSpringBootTest {
    private final LogCaptor logCaptor = forClass(GeocodingLoggingAspectTest.class);

    @Autowired
    private SuccessGeocoder successGeocoder;

    @Autowired
    private FailureGeocoder failureGeocoder;

    @Test
    public void successGeocodingShouldBeLogged() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 5.5);

        successGeocoder.geocode(givenCoordinate);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of();
        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    public void failedReceivingShouldBeLogged() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 6.6);

        failureGeocoder.geocode(givenCoordinate);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of();
        assertEquals(expectedLogs, actualLogs);
    }

    @Component
    public static class SuccessGeocoder implements Geocoder {
        private static final Address ADDRESS = Address.builder()
                .id(255L)
                .cityName("City")
                .countryName("Country")
                .build();

        @Override
        public Optional<Address> geocode(final GpsCoordinate coordinate) {
            return Optional.of(ADDRESS);
        }
    }

    @Component
    public static class FailureGeocoder implements Geocoder {

        @Override
        public Optional<Address> geocode(final GpsCoordinate coordinate) {
            return empty();
        }
    }
}
