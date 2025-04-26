package by.vladzuev.locationreceiver.protocol.jt808.handler;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808Location;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808LocationPackage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class JT808LocationPackageHandlerTest {
    private final JT808LocationPackageHandler handler = new JT808LocationPackageHandler(null, null, null, null);

    @Test
    public void locationSourcesShouldBeFound() {
        final var givenLocations = List.of(JT808Location.builder().build(), JT808Location.builder().build());
        final JT808LocationPackage givenRequest = new JT808LocationPackage(givenLocations);

        final List<JT808Location> actual = handler.streamLocationSources(givenRequest);
        assertSame(givenLocations, actual);
    }

    @Test
    public void dateShouldBeFound() {
        final JT808Location givenLocation = JT808Location.builder()
                .dateTime(LocalDateTime.of(2025, 10, 9, 8, 7, 6))
                .build();

        final Optional<LocalDate> optionalActual = handler.findDate(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalDate actual = optionalActual.get();
        final LocalDate expected = LocalDate.of(2025, 10, 9);
        assertEquals(expected, actual);
    }

    @Test
    public void timeShouldBeFound() {
        final JT808Location givenLocation = JT808Location.builder()
                .dateTime(LocalDateTime.of(2025, 10, 9, 8, 7, 6))
                .build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalTime actual = optionalActual.get();
        final LocalTime expected = LocalTime.of(8, 7, 6);
        assertEquals(expected, actual);
    }

    @Test
    public void latitudeShouldBeFound() {
        final double givenLatitude = 5.5;
        final JT808Location givenLocation = JT808Location.builder().latitude(givenLatitude).build();

        final OptionalDouble optionalActual = handler.findLatitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLatitude, actual);
    }

    @Test
    public void longitudeShouldBeFound() {
        final double givenLongitude = 6.6;
        final JT808Location givenLocation = JT808Location.builder().longitude(givenLongitude).build();

        final OptionalDouble optionalActual = handler.findLongitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLongitude, actual);
    }

    @Test
    public void courseShouldBeFound() {
        final short givenCourse = 20;
        final JT808Location givenLocation = JT808Location.builder().course(givenCourse).build();

        final OptionalInt optionalActual = handler.findCourse(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenCourse, actual);
    }

    @Test
    public void speedShouldBeFound() {
        final short givenSpeed = 50;
        final JT808Location givenLocation = JT808Location.builder().speed(givenSpeed).build();

        final OptionalDouble optionalActual = handler.findSpeed(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenSpeed, actual);
    }

    @Test
    public void altitudeShouldBeFound() {
        final short givenAltitude = 30;
        final JT808Location givenLocation = JT808Location.builder().altitude(givenAltitude).build();

        final OptionalInt optionalActual = handler.findAltitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAltitude, actual);
    }

    @Test
    public void satelliteCountShouldBeFound() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void hdopShouldNotBeFound() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void inputsShouldNotBeFound() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final OptionalInt optionalActual = handler.findInputs(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void outputsShouldNotBeFound() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final OptionalInt optionalActual = handler.findOutputs(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void analogInputsShouldBeGot() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final double[] actual = handler.getAnalogInputs(givenLocation);
        assertEquals(0, actual.length);
    }

    @Test
    public void driverKeyCodeShouldNotBeFound() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void parametersShouldBeGot() {
        final JT808Location givenLocation = mock(JT808Location.class);

        final Stream<Parameter> actual = handler.streamParameters(givenLocation);
        final boolean actualEmpty = actual.findFirst().isEmpty();
        assertTrue(actualEmpty);

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void responseShouldBeCreated() {
        throw new UnsupportedOperationException();
    }
}
