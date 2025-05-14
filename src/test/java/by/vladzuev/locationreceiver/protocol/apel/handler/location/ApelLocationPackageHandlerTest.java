package by.vladzuev.locationreceiver.protocol.apel.handler.location;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class ApelLocationPackageHandlerTest {
    private final TestApelLocationPackageHandler handler = new TestApelLocationPackageHandler();

    @Test
    public void locationSourcesShouldBeStreamed() {
        final ApelLocation givenLocation = ApelLocation.builder().latitude(5).longitude(6).build();
        final TestPackage givenRequest = new TestPackage(givenLocation);

        final Stream<ApelLocation> actual = handler.streamLocationSources(givenRequest);
        final List<ApelLocation> actualAsList = actual.toList();
        final List<ApelLocation> expectedAsList = List.of(givenLocation);
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void dateShouldBeFound() {
        final ApelLocation givenLocation = ApelLocation.builder().epochSeconds(1747226571).build();

        final Optional<LocalDate> optionalActual = handler.findDate(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalDate actual = optionalActual.get();
        final LocalDate expected = LocalDate.of(2025, 5, 14);
        assertEquals(expected, actual);
    }

    @Test
    public void timeShouldBeFound() {
        final ApelLocation givenLocation = ApelLocation.builder().epochSeconds(1747226571).build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalTime actual = optionalActual.get();
        final LocalTime expected = LocalTime.of(12, 42, 51);
        assertEquals(expected, actual);
    }

    @Test
    public void latitudeShouldBeFound() {
        final ApelLocation givenLocation = ApelLocation.builder().latitude(-483284898).build();

        final OptionalDouble optionalActual = handler.findLatitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = -5.765813418317017E15;
        assertEquals(expected, actual);
    }

    @Test
    public void longitudeShouldBeFound() {
        final ApelLocation givenLocation = ApelLocation.builder().longitude(2138535798).build();

        final OptionalDouble optionalActual = handler.findLongitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = 2.5513725859606084E16;
        assertEquals(expected, actual);
    }

    @Test
    public void courseShouldBeFound() {
        final short givenCourse = 50;
        final ApelLocation givenLocation = ApelLocation.builder().course(givenCourse).build();

        final OptionalInt optionalActual = handler.findCourse(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenCourse, actual);
    }

    @Test
    public void speedShouldBeFound() {
        final short givenSpeed = 50;
        final ApelLocation givenLocation = ApelLocation.builder().speed(givenSpeed).build();

        final OptionalDouble optionalActual = handler.findSpeed(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenSpeed, actual);
    }

    @Test
    public void altitudeShouldBeFound() {
        final short givenAltitude = 55;
        final ApelLocation givenLocation = ApelLocation.builder().altitude(givenAltitude).build();

        final OptionalInt optionalActual = handler.findAltitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAltitude, actual);
    }

    @Test
    public void satelliteCountShouldBeFound() {
        final byte givenSatelliteCount = 10;
        final ApelLocation givenLocation = ApelLocation.builder().satelliteCount(givenSatelliteCount).build();

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenSatelliteCount, actual);
    }

    @Test
    public void satelliteCountShouldNotBeFound() {
        final ApelLocation givenLocation = ApelLocation.builder().build();

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeFound() {
        final byte givenHdop = 5;
        final ApelLocation givenLocation = ApelLocation.builder().hdop(givenHdop).build();

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenHdop, actual);
    }

    @Test
    public void hdopShouldNotBeFound() {
        final ApelLocation givenLocation = ApelLocation.builder().build();

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void inputsShouldBeFound() {
        final ApelLocation givenLocation = mock(ApelLocation.class);

        final OptionalInt optionalActual = handler.findInputs(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void outputsShouldBeFound() {
        final ApelLocation givenLocation = mock(ApelLocation.class);

        final OptionalInt optionalActual = handler.findOutputs(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void analogInputsShouldBeGot() {
        final double[] givenAnalogInputs = {1, 2, 3};
        final ApelLocation givenLocation = ApelLocation.builder().analogInputs(givenAnalogInputs).build();

        final double[] actual = handler.getAnalogInputs(givenLocation);
        assertSame(givenAnalogInputs, actual);
    }

    @Test
    public void driverKeyCodeShouldBeFound() {
        final ApelLocation givenLocation = mock(ApelLocation.class);

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void parametersShouldBeStreamed() {
        final ApelLocation givenLocation = mock(ApelLocation.class);

        final Stream<Parameter> actual = handler.streamParameters(givenLocation);
        final boolean actualEmpty = actual.findFirst().isEmpty();
        assertTrue(actualEmpty);

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void successShouldBeHandled() {
        handler.onSuccess();
    }

    @Value
    private static class TestPackage {
        ApelLocation location;
    }

    private static final class TestApelLocationPackageHandler extends ApelLocationPackageHandler<TestPackage> {

        public TestApelLocationPackageHandler() {
            super(TestPackage.class, null, null, null, null);
        }

        @Override
        protected ApelLocation getLocation(final TestPackage request) {
            return request.getLocation();
        }

        @Override
        protected Object createSuccessResponse(final TestPackage request) {
            throw new UnsupportedOperationException();
        }
    }
}
