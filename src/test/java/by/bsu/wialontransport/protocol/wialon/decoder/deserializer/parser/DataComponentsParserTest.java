package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.DataEntity;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataComponentsParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static java.time.LocalDateTime.MIN;
import static org.junit.Assert.*;

public final class DataComponentsParserTest {

    @Test
    public void parserShouldBeCreatedByData() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        new DataComponentsParser(givenData);
    }

    @Test
    public void parserShouldBeCreatedByDataWithNotDefinedComponents() {
        final String givenData = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;;";
        new DataComponentsParser(givenData);
    }

    @Test
    public void parserShouldBeCreatedByDataWithNotDefinedComponentsWithNAAsAnalogInputs() {
        final String givenData = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;";
        new DataComponentsParser(givenData);
    }

    @Test(expected = NotValidDataException.class)
    public void parserShouldNotBeCreatedByNotValidData() {
        final String givenData = "not valid";
        new DataComponentsParser(givenData);
    }

    @Test
    public void dateTimeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final LocalDateTime actual = givenParser.parseDateTime();
        final LocalDateTime expected = LocalDateTime.of(
                2022, 11, 15, 14, 56, 43);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateTimeShouldBeParsed() {
        final String givenData = "NA;NA;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final LocalDateTime actual = givenParser.parseDateTime();
        assertEquals(MIN, actual);
    }

    @Test
    public void latitudeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final Latitude actual = givenParser.parseLatitude();
        final Latitude expected = Latitude.builder()
                .degrees(55)
                .minutes(44)
                .minuteShare(6025)
                .type(NORTH)
                .build();

        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedLatitudeShouldBeParsed() {
        final String givenData = "151122;145643;NA;NA;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final Latitude actual = givenParser.parseLatitude();
        final Latitude expected = Latitude.builder()
                .degrees(Integer.MIN_VALUE)
                .minutes(Integer.MIN_VALUE)
                .minuteShare(Integer.MIN_VALUE)
                .type(DataEntity.Latitude.Type.NOT_DEFINED)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void longitudeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final Longitude actual = givenParser.parseLongitude();
        final Longitude expected = Longitude.builder()
                .degrees(37)
                .minutes(39)
                .minuteShare(6834)
                .type(EAST)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedLongitudeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;NA;NA;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final Longitude actual = givenParser.parseLongitude();
        final Longitude expected = Longitude.builder()
                .degrees(Integer.MIN_VALUE)
                .minutes(Integer.MIN_VALUE)
                .minuteShare(Integer.MIN_VALUE)
                .type(DataEntity.Longitude.Type.NOT_DEFINED)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseSpeed();
        final int expected = 100;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseSpeed();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseCourse();
        final int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseCourse();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseAltitude();
        final int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAltitudeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseAltitude();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test
    public void amountSatellitesShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseAmountSatellites();
        final int expected = 177;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAmountSatellitesShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseAmountSatellites();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test
    public void reductionPrecisionShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final double actual = givenParser.parseReductionPrecision();
        final double expected = 545.4554;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedReductionPrecisionShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;NA;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final double actual = givenParser.parseReductionPrecision();
        final double expected = Double.MIN_VALUE;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void inputsShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseInputs();
        final int expected = 17;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedInputsShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;NA;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseInputs();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test
    public void outputsShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseOutputs();
        final int expected = 18;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedOutputsShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;NA;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final int actual = givenParser.parseOutputs();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test
    public void analogInputsShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final double[] actual = givenParser.parseAnalogInputs();
        final double[] expected = new double[]{5.5, 4343.454544334, 454.433, 1};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedAnalogInputsShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + ";"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final double[] actual = givenParser.parseAnalogInputs();
        assertEquals(0, actual.length);
    }

    @Test
    public void notDefinedAnalogInputsAsNAShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "NA;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final double[] actual = givenParser.parseAnalogInputs();
        assertEquals(0, actual.length);
    }

    @Test
    public void driverKeyCodeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final String actual = givenParser.parseDriverKeyCode();
        final String expected = "keydrivercode";
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDriverKeyCodeShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "NA;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final String actual = givenParser.parseDriverKeyCode();
        final String expected = "not defined";
        assertEquals(expected, actual);
    }

    @Test
    public void parametersShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final Map<String, Parameter> actual = givenParser.parseParameters();
        final Map<String, Parameter> expected = Map.of(
                "param-name-1", createParameter("param-name-1", INTEGER, "654321"),
                "param-name-2", createParameter("param-name-2", DOUBLE, "65.4321"),
                "param-name-3", createParameter("param-name-3", STRING, "param-value")
        );
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedParametersShouldBeParsed() {
        final String givenData = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;";
        final DataComponentsParser givenParser = new DataComponentsParser(givenData);

        final Map<String, Parameter> actual = givenParser.parseParameters();
        assertTrue(actual.isEmpty());
    }

    private static Parameter createParameter(final String name, final Type type, final String value) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .build();
    }
}
