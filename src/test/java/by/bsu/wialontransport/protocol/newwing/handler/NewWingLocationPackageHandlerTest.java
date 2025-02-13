package by.bsu.wialontransport.protocol.newwing.handler;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLocationPackage;
import by.bsu.wialontransport.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public final class NewWingLocationPackageHandlerTest {
    private final NewWingLocationPackageHandler handler = new NewWingLocationPackageHandler(null, null, null, null);

    @Test
    public void locationSourcesShouldBeGot() {
        final var givenLocations = List.of(NewWingLocation.builder().build(), NewWingLocation.builder().build());
        final NewWingLocationPackage givenRequest = new NewWingLocationPackage(givenLocations);

        final List<NewWingLocation> actual = handler.getLocationSources(givenRequest);
        assertSame(givenLocations, actual);
    }

    @Test
    public void dateShouldBeFound() {
        final LocalDate givenDate = LocalDate.of(2024, 11, 12);
        final NewWingLocation givenLocation = NewWingLocation.builder().date(givenDate).build();

        final Optional<LocalDate> optionalActual = handler.findDate(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalDate actual = optionalActual.get();
        assertSame(givenDate, actual);
    }

    @Test
    public void timeShouldBeFound() {
        final LocalTime givenTime = LocalTime.of(9, 37, 38);
        final NewWingLocation givenLocation = NewWingLocation.builder().time(givenTime).build();

        final Optional<LocalTime> optionalActual = handler.findTime(givenLocation);
        assertTrue(optionalActual.isPresent());
        final LocalTime actual = optionalActual.get();
        assertSame(givenTime, actual);
    }

    @Test
    public void latitudeShouldBeFound() {
        final double givenLatitude = 5.5;
        final NewWingLocation givenLocation = NewWingLocation.builder().latitude(givenLatitude).build();

        final OptionalDouble optionalActual = handler.findLatitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLatitude, actual);
    }

    @Test
    public void longitudeShouldBeFound() {
        final double givenLongitude = 6.6;
        final NewWingLocation givenLocation = NewWingLocation.builder().longitude(givenLongitude).build();

        final OptionalDouble optionalActual = handler.findLongitude(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenLongitude, actual);
    }

    @Test
    public void courseShouldBeFound() {
        final short givenCourse = 10;
        final NewWingLocation givenLocation = NewWingLocation.builder().course(givenCourse).build();

        final OptionalInt optionalActual = handler.findCourse(givenLocation);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenCourse, actual);
    }

    @Test
    public void speedShouldBeFound() {
        final double givenSpeed = 50;
        final NewWingLocation givenLocation = NewWingLocation.builder().speed(givenSpeed).build();

        final OptionalDouble optionalActual = handler.findSpeed(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenSpeed, actual);
    }

    @Test
    public void altitudeShouldNotBeFound() {
        final NewWingLocation givenLocation = NewWingLocation.builder().build();

        final OptionalInt optionalActual = handler.findAltitude(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void satelliteCountShouldNotBeFound() {
        final NewWingLocation givenLocation = NewWingLocation.builder().build();

        final OptionalInt optionalActual = handler.findSatelliteCount(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeFound() {
        final double givenHdop = 5.5;
        final NewWingLocation givenLocation = NewWingLocation.builder().hdop(givenHdop).build();

        final OptionalDouble optionalActual = handler.findHdop(givenLocation);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenHdop, actual);
    }

    @Test
    public void inputsShouldNotBeFound() {
        final NewWingLocation givenLocation = NewWingLocation.builder().build();

        final OptionalInt optionalActual = handler.findInputs(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void outputsShouldNotBeFound() {
        final NewWingLocation givenLocation = NewWingLocation.builder().build();

        final OptionalInt optionalActual = handler.findOutputs(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void analogInputsShouldBeFound() {
        final double[] givenAnalogInputs = {1, 2, 3, 4};
        final NewWingLocation givenLocation = NewWingLocation.builder().analogInputs(givenAnalogInputs).build();

        final double[] actual = handler.getAnalogInputs(givenLocation);
        assertSame(givenAnalogInputs, actual);
    }

    @Test
    public void driverKeyCodeShouldNotBeFound() {
        final NewWingLocation givenLocation = NewWingLocation.builder().build();

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenLocation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersShouldBeGot() {
        final NewWingLocation givenLocation = NewWingLocation.builder().build();

        final Stream<Parameter> actual = handler.getParameters(givenLocation);
        final boolean actualEmpty = actual.findAny().isEmpty();
        assertTrue(actualEmpty);
    }

    @Test
    public void responseShouldBeCreated() {
        final int givenLocationCount = 15;

        final NewWingSuccessResponsePackage actual = handler.createResponse(givenLocationCount);
        final NewWingSuccessResponsePackage expected = new NewWingSuccessResponsePackage();
        assertEquals(expected, actual);
    }
}
