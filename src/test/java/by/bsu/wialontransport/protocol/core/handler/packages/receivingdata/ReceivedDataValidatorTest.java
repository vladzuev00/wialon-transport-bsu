package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.model.ReceivedData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

public final class ReceivedDataValidatorTest extends AbstractContextTest {

    @Autowired
    private ReceivedDataValidator validator;

    @Test
    public void dataShouldBeValid() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfLatitudeIsLessThanMinimalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(-90.1, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfLatitudeIsMoreThanMaximalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(90.1, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfLongitudeIsLessThanMinimalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, -180.1))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, 180.1))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfAmountOfSatellitesIsLessThanMinimalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(2)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(1000)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfDateTimeIsLessThanMinimalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(LocalDateTime.of(2009, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfDateTimeIsMoreThanMaximalAllowable() {
        final ReceivedData givenData = ReceivedData.builder()
                .dateTime(now().plusSeconds(16))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(emptyMap())
                .build();

        final boolean actual = validator.isValid(givenData);
        final boolean expected = false;
        assertEquals(expected, actual);
    }
}
