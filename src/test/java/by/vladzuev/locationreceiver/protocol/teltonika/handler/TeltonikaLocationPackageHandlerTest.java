package by.vladzuev.locationreceiver.protocol.teltonika.handler;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.protocol.teltonika.holder.TeltonikaLoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaRequestLocationPackage;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaResponseLocationPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class TeltonikaLocationPackageHandlerTest {

    @Mock
    private TeltonikaLoginSuccessHolder mockedLoginSuccessHolder;

    private TeltonikaLocationPackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TeltonikaLocationPackageHandler(null, null, null, null, mockedLoginSuccessHolder);
    }

    @Test
    public void locationSourcesShouldBeStreamed() {
        final var firstGivenLocation = new TeltonikaLocation(now(), 5.5, 6.6, (short) 7, (short) 8, (byte) 9, (short) 10);
        final var secondGivenLocation = new TeltonikaLocation(now(), 6.6, 7.7, (short) 8, (short) 9, (byte) 10, (short) 11);
        final var givenRequest = new TeltonikaRequestLocationPackage(List.of(firstGivenLocation, secondGivenLocation));

        final Stream<TeltonikaLocation> actual = handler.streamLocationSources(givenRequest);
        final List<TeltonikaLocation> actualAsList = actual.toList();
        final List<TeltonikaLocation> expectedAsList = List.of(firstGivenLocation, secondGivenLocation);
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void dateShouldBeFound() {
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder()
                .dateTime(LocalDateTime.of(2025, 5, 5, 12, 0, 0))
                .build();

        final Optional<LocalDate> optionalActual = handler.findDate(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalDate actual = optionalActual.get();
        final LocalDate expected = LocalDate.of(2025, 5, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void timeShouldBeFound() {
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder()
                .dateTime(LocalDateTime.of(2025, 5, 5, 12, 0, 0))
                .build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalTime actual = optionalActual.get();
        final LocalTime expected = LocalTime.of(12, 0, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void latitudeShouldBeFound() {
        final double givenLatitude = 5.5;
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder().latitude(givenLatitude).build();

        final OptionalDouble optionalActual = handler.findLatitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLatitude, actual);
    }

    @Test
    public void longitudeShouldBeFound() {
        final double givenLongitude = 5.5;
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder().longitude(givenLongitude).build();

        final OptionalDouble optionalActual = handler.findLongitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLongitude, actual);
    }

    @Test
    public void courseShouldBeFound() {
        final short givenAngle = 6;
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder().angle(givenAngle).build();

        final OptionalInt optionalActual = handler.findCourse(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAngle, actual);
    }

    @Test
    public void speedShouldBeFound() {
        final short givenSpeed = 6;
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder().speed(givenSpeed).build();

        final OptionalDouble optionalActual = handler.findSpeed(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenSpeed, actual);
    }

    @Test
    public void altitudeShouldBeFound() {
        final short givenAltitude = 6;
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder().altitude(givenAltitude).build();

        final OptionalInt optionalActual = handler.findAltitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAltitude, actual);
    }

    @Test
    public void satelliteCountShouldBeFound() {
        final byte givenSatelliteCount = 6;
        final TeltonikaLocation givenLocation = TeltonikaLocation.builder().satelliteCount(givenSatelliteCount).build();

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenSatelliteCount, actual);
    }

    @Test
    public void hdopShouldBeFound() {
        final TeltonikaLocation givenLocation = mock(TeltonikaLocation.class);

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void inputsShouldBeFound() {
        final TeltonikaLocation givenLocation = mock(TeltonikaLocation.class);

        final OptionalInt optionalActual = handler.findInputs(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void outputsShouldBeFound() {
        final TeltonikaLocation givenLocation = mock(TeltonikaLocation.class);

        final OptionalInt optionalActual = handler.findOutputs(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void analogInputsShouldBeFound() {
        final TeltonikaLocation givenLocation = mock(TeltonikaLocation.class);

        final double[] actual = handler.getAnalogInputs(givenLocation);
        final double[] expected = {};
        assertArrayEquals(expected, actual);

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void driverKeyCodeShouldBeFound() {
        final TeltonikaLocation givenLocation = mock(TeltonikaLocation.class);

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenLocation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(givenLocation);
    }

    @Test
    public void parametersShouldBeStreamed() {
        final TeltonikaLocation givenLocation = mock(TeltonikaLocation.class);

        final Stream<Parameter> actual = handler.streamParameters(givenLocation);
        final boolean actualEmpty = actual.findFirst().isEmpty();
        assertTrue(actualEmpty);
    }

    @Test
    public void successShouldBeHandled() {
        handler.onSuccess();

        verify(mockedLoginSuccessHolder, times(1)).setSuccess(eq(false));
    }

    @Test
    public void successResponseShouldBeCreated() {
        final TeltonikaRequestLocationPackage givenRequest = new TeltonikaRequestLocationPackage(
                List.of(
                        TeltonikaLocation.builder().build(),
                        TeltonikaLocation.builder().build(),
                        TeltonikaLocation.builder().build()
                )
        );

        final TeltonikaResponseLocationPackage actual = handler.createSuccessResponse(givenRequest);
        final TeltonikaResponseLocationPackage expected = new TeltonikaResponseLocationPackage(3);
        assertEquals(expected, actual);
    }
}
