package by.bsu.wialontransport.service.geocoding.aspect;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.service.geocoding.service.GeocodingService;
import nl.altindag.log.LogCaptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.Assert.assertEquals;

@Import(
        {
                GeocodingServiceAspectTest.SuccessReceivingService.class,
                GeocodingServiceAspectTest.FailedReceivingService.class
        }
)
public final class GeocodingServiceAspectTest extends AbstractSpringBootTest {

    @Autowired
    private SuccessReceivingService successReceivingService;

    @Autowired
    private FailedReceivingService failedReceivingService;

    @Test
    public void successReceivingShouldBeLogged() {
        try (final LogCaptor givenLogCaptor = createLogCaptor()) {
            final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 5.5);

            successReceivingService.receive(givenCoordinate);

            final List<String> actualLogs = givenLogCaptor.getLogs();
            final List<String> expectedLogs = List.of(
                    "Address 'Address(id=null, boundingBox=null, center=null, cityName=null, countryName=null, "
                            + "geometry=null)' was successfully received by 'SuccessReceivingService'"
            );
            assertEquals(expectedLogs, actualLogs);
        }
    }

    @Test
    public void failedReceivingShouldBeLogged() {
        try (final LogCaptor givenLogCaptor = createLogCaptor()) {
            final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 6.6);

            failedReceivingService.receive(givenCoordinate);

            final List<String> actualLogs = givenLogCaptor.getLogs();
            final List<String> expectedLogs = List.of("Address wasn't received by 'FailedReceivingService'");
            assertEquals(expectedLogs, actualLogs);
        }
    }

    private static LogCaptor createLogCaptor() {
        return forClass(GeocodingServiceAspect.class);
    }

    @Service
    public static class SuccessReceivingService implements GeocodingService {

        @Override
        public Optional<Address> receive(final GpsCoordinate coordinate) {
            final Address address = Address.builder().build();
            return Optional.of(address);
        }
    }

    @Service
    public static class FailedReceivingService implements GeocodingService {

        @Override
        public Optional<Address> receive(final GpsCoordinate coordinate) {
            return empty();
        }
    }
}
