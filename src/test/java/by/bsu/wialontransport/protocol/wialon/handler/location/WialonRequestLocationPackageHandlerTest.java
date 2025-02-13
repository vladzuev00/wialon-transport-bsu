package by.bsu.wialontransport.protocol.wialon.handler.location;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestLocationPackage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public final class WialonRequestLocationPackageHandlerTest {
    private final TestWialonRequestLocationPackageHandler handler = new TestWialonRequestLocationPackageHandler();

    @Test
    public void locationSourcesShouldBeGot() {
        final var givenSources = List.of(WialonLocation.builder().build(), WialonLocation.builder().build());
        final TestPackage givenRequest = new TestPackage(givenSources);

        final List<WialonLocation> actual = handler.getLocationSources(givenRequest);
        assertSame(givenSources, actual);
    }

    @Test
    public void dateShouldBeFound() {
        final LocalDate givenDate = LocalDate.of(2024, 10, 11);
        final WialonLocation givenLocation = WialonLocation.builder().date(givenDate).build();

        final Optional<LocalDate> optionalActual = handler.findDate(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalDate actual = optionalActual.get();
        assertSame(givenDate, actual);
    }

    @Test
    public void dateShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void timeShouldBeFound() {
        final LocalTime givenTime = LocalTime.of(10, 38, 39);
        final WialonLocation givenLocation = WialonLocation.builder().time(givenTime).build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalTime actual = optionalActual.get();
        assertSame(givenTime, actual);
    }

    @Test
    public void timeShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void latitudeShouldBeFound() {
        final double givenLatitude = 5.5;
        final WialonLocation givenLocation = WialonLocation.builder().latitude(givenLatitude).build();

        final OptionalDouble optionalActual = handler.findLatitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLatitude, actual);
    }

    @Test
    public void latitudeShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalDouble optionalActual = handler.findLatitude(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void longitudeShouldBeFound() {
        final double givenLongitude = 6.6;
        final WialonLocation givenLocation = WialonLocation.builder().longitude(givenLongitude).build();

        final OptionalDouble optionalActual = handler.findLongitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLongitude, actual);
    }

    @Test
    public void longitudeShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalDouble optionalActual = handler.findLongitude(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void courseShouldBeFound() {
        final int givenCourse = 10;
        final WialonLocation givenLocation = WialonLocation.builder().course(givenCourse).build();

        final OptionalInt optionalActual = handler.findCourse(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenCourse, actual);
    }

    @Test
    public void courseShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalInt optionalActual = handler.findCourse(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void speedShouldBeFound() {
        final double givenSpeed = 10.5;
        final WialonLocation givenLocation = WialonLocation.builder().speed(givenSpeed).build();

        final OptionalDouble optionalActual = handler.findSpeed(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenSpeed, actual);
    }

    @Test
    public void speedShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalDouble optionalActual = handler.findSpeed(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void altitudeShouldBeFound() {
        final int givenAltitude = 105;
        final WialonLocation givenLocation = WialonLocation.builder().altitude(givenAltitude).build();

        final OptionalInt optionalActual = handler.findAltitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAltitude, actual);
    }

    @Test
    public void altitudeShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalInt optionalActual = handler.findAltitude(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void satelliteCountShouldBeFound() {
        final int givenSatelliteCount = 15;
        final WialonLocation givenLocation = WialonLocation.builder().satelliteCount(givenSatelliteCount).build();

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenSatelliteCount, actual);
    }

    @Test
    public void satelliteCountShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeFound() {
        final double givenHdop = 5.5;
        final WialonLocation givenLocation = WialonLocation.builder().hdop(givenHdop).build();

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenHdop, actual);
    }

    @Test
    public void hdopShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void inputsShouldBeFound() {
        final int givenInputs = 10;
        final WialonLocation givenLocation = WialonLocation.builder().inputs(givenInputs).build();

        final OptionalInt optionalActual = handler.findInputs(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenInputs, actual);
    }

    @Test
    public void inputsShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalInt optionalActual = handler.findInputs(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void outputsShouldBeFound() {
        final int givenOutputs = 10;
        final WialonLocation givenLocation = WialonLocation.builder().outputs(givenOutputs).build();

        final OptionalInt optionalActual = handler.findOutputs(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenOutputs, actual);
    }

    @Test
    public void outputsShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final OptionalInt optionalActual = handler.findOutputs(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void analogInputsShouldBeFound() {
        final double[] givenAnalogInputs = {1, 2, 3, 4};
        final WialonLocation givenLocation = WialonLocation.builder().analogInputs(givenAnalogInputs).build();

        final double[] actual = handler.getAnalogInputs(givenLocation);
        assertArrayEquals(givenAnalogInputs, actual);
    }

    @Test
    public void driverKeyCodeShouldBeFound() {
        final String givenDriverKeyCode = "driver-key-code";
        final WialonLocation givenLocation = WialonLocation.builder().driverKeyCode(givenDriverKeyCode).build();

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenLocation);
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        assertSame(givenDriverKeyCode, actual);
    }

    @Test
    public void driverKeyCodeShouldNotBeFound() {
        final WialonLocation givenLocation = WialonLocation.builder().build();

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersShouldBeGot() {
        final Set<Parameter> givenParameters = Set.of(mock(Parameter.class), mock(Parameter.class));
        final WialonLocation givenLocation = WialonLocation.builder().parameters(givenParameters).build();

        final Stream<Parameter> actual = handler.getParameters(givenLocation);
        final Set<Parameter> actualAsSet = actual.collect(toUnmodifiableSet());
        assertEquals(givenParameters, actualAsSet);
    }

    private static final class TestPackage extends WialonRequestLocationPackage {

        public TestPackage(final List<WialonLocation> locations) {
            super(locations);
        }

        @Override
        public String getPrefix() {
            throw new UnsupportedOperationException();
        }
    }

    private static final class TestWialonRequestLocationPackageHandler extends WialonRequestLocationPackageHandler<TestPackage> {

        public TestWialonRequestLocationPackageHandler() {
            super(TestPackage.class, null, null, null, null);
        }

        @Override
        protected Object createResponse(final int locationCount) {
            throw new UnsupportedOperationException();
        }
    }
}
