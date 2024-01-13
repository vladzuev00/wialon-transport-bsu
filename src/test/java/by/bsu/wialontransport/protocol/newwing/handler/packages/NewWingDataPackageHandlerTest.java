package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.StreamUtil.isEmpty;
import static org.junit.Assert.*;

public final class NewWingDataPackageHandlerTest {
    private final NewWingDataPackageHandler handler = new NewWingDataPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void sourcesShouldBeGot() {
        final List<NewWingData> givenSources = List.of(
                NewWingData.builder()
                        .day((byte) 12)
                        .month((byte) 1)
                        .year((byte) 24)
                        .build(),
                NewWingData.builder()
                        .day((byte) 13)
                        .month((byte) 2)
                        .year((byte) 25)
                        .build(),
                NewWingData.builder()
                        .day((byte) 14)
                        .month((byte) 3)
                        .year((byte) 26)
                        .build()
        );
        final NewWingDataPackage givenRequestPackage = new NewWingDataPackage(0, givenSources);

        final Stream<NewWingData> actual = handler.getSources(givenRequestPackage);
        final List<NewWingData> actualAsList = actual.toList();
        assertEquals(givenSources, actualAsList);
    }

    @Test
    public void dateTimeShouldBeGot() {
        final NewWingData givenNewWingData = NewWingData.builder()
                .day((byte) 12)
                .month((byte) 1)
                .year((byte) 24)
                .hour((byte) 12)
                .minute((byte) 13)
                .second((byte) 14)
                .build();

        final LocalDateTime actual = handler.getDateTime(givenNewWingData);
        final LocalDateTime expected = LocalDateTime.of(2024, 1, 12, 12, 13, 14);
        assertEquals(expected, actual);
    }

    @Test
    public void coordinateShouldBeGot() {
        final NewWingData givenNewWingData = NewWingData.builder()
                .latitudeIntegerPart((short) 5354)
                .latitudeFractionalPart((short) 1978)
                .longitudeIntegerPart((short) 2733)
                .longitudeFractionalPart((short) 1739)
                .build();

        final Coordinate actual = handler.getCoordinate(givenNewWingData);
        final Coordinate expected = new Coordinate(53.91630172729492, 27.56231689453125);
        assertEquals(expected, actual);
    }

    @Test
    public void courseShouldBeFound() {
        final int givenCourse = 4;
        final NewWingData givenNewWingData = NewWingData.builder()
                .course((short) givenCourse)
                .build();

        final OptionalInt optionalActual = handler.findCourse(givenNewWingData);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenCourse, actual);
    }

    @Test
    public void speedShouldBeFound() {
        final short givenIntegerPart = 4;
        final byte givenFractionalPart = 5;
        final NewWingData givenNewWingData = NewWingData.builder()
                .speedIntegerPart(givenIntegerPart)
                .speedFractionalPart(givenFractionalPart)
                .build();

        final OptionalDouble optionalActual = handler.findSpeed(givenNewWingData);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = 4.5;
        assertEquals(expected, actual, 0);
    }

    @Test
    public void altitudeShouldNotBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder().build();

        final OptionalInt optionalActual = handler.findAltitude(givenNewWingData);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void amountOfSatellitesShouldNotBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder().build();

        final OptionalInt optionalActual = handler.findAmountOfSatellites(givenNewWingData);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder()
                .hdopIntegerPart((byte) 2)
                .hdopFractionalPart((byte) 123)
                .build();

        final OptionalDouble optionalActual = handler.findHdop(givenNewWingData);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = 2.123;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void inputsShouldNotBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder().build();

        final OptionalInt optionalActual = handler.findInputs(givenNewWingData);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void outputsShouldNotBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder().build();

        final OptionalInt optionalActual = handler.findOutputs(givenNewWingData);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void analogInputsShouldBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder()
                .firstAnalogInputLevel((short) 156)
                .secondAnalogInputLevel((short) 54)
                .thirdAnalogInputLevel((short) 34)
                .fourthAnalogInputLevel((short) 439)
                .build();

        final Optional<double[]> optionalActual = handler.findAnalogInputs(givenNewWingData);
        assertTrue(optionalActual.isPresent());
        final double[] actual = optionalActual.get();
        final double[] expected = new double[]{0.156, 0.054, 0.034, 0.439};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void driverKeyCodeShouldBeFound() {
        final NewWingData givenNewWingData = NewWingData.builder().build();

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenNewWingData);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersShouldBeGot() {
        final NewWingData givenNewWingData = NewWingData.builder().build();

        final Stream<Parameter> actual = handler.getParameters(givenNewWingData);
        assertTrue(isEmpty(actual));
    }
}
