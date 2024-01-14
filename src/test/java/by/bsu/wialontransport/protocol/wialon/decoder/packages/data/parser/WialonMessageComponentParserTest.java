package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType.EAST;
import static org.junit.Assert.*;

public final class WialonMessageComponentParserTest {
    private static final LocalDate EXPECTED_NOT_DEFINED_DATE = LocalDate.MIN;
    private static final LocalTime EXPECTED_NOT_DEFINED_TIME = LocalTime.MIN;
    private static final int EXPECTED_NOT_DEFINED_DEGREES = Integer.MIN_VALUE;
    private static final int EXPECTED_NOT_DEFINED_MINUTES = Integer.MIN_VALUE;
    private static final int EXPECTED_NOT_DEFINED_MINUTE_SHARE = Integer.MIN_VALUE;
    private static final LatitudeType EXPECTED_NOT_DEFINED_LATITUDE_TYPE = NORTH;
    private static final LongitudeType EXPECTED_NOT_DEFINED_LONGITUDE_TYPE = EAST;

    @Test
    public void parserShouldBeCreated() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";

        new WialonMessageComponentParser(givenMessage);
    }

    @Test
    public void parserShouldBeCreatedByMessageWithNotDefinedComponents() {
        final String givenMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;;";

        new WialonMessageComponentParser(givenMessage);
    }

    @Test
    public void parserShouldBeCreatedByMessageWithNotDefinedComponentsAndWithNAAsAnalogInputs() {
        final String givenMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;";

        new WialonMessageComponentParser(givenMessage);
    }

    @Test(expected = NotValidMessageException.class)
    public void parserShouldNotBeCreatedByNotValidMessage() {
        final String givenMessage = "not valid";

        new WialonMessageComponentParser(givenMessage);
    }

    @Test(expected = NotValidMessageException.class)
    public void parserShouldNotBeCreatedBecauseOfThereIsNoParametersDelimiter() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321param-name-2:2:65.4321,param-name-3:3:param-value";

        new WialonMessageComponentParser(givenMessage);
    }

    @Test
    public void dateShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final LocalDate actual = givenParser.parseDate();
        final LocalDate expected = LocalDate.of(2022, 11, 15);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateShouldBeParsed() {
        final String givenMessage = "NA;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final LocalDate actual = givenParser.parseDate();
        assertSame(EXPECTED_NOT_DEFINED_DATE, actual);
    }

    @Test
    public void timeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final LocalTime actual = givenParser.parseTime();
        final LocalTime expected = LocalTime.of(14, 56, 43);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedTimeShouldBeParsed() {
        final String givenMessage = "151122;NA;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final LocalTime actual = givenParser.parseTime();
        assertSame(EXPECTED_NOT_DEFINED_TIME, actual);
    }

    @Test
    public void latitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Latitude actual = givenParser.parseLatitude();
        final Latitude expected = new Latitude(55, 44, 6025, NORTH);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedLatitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;NA;NA;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Latitude actual = givenParser.parseLatitude();
        final Latitude expected = new Latitude(
                EXPECTED_NOT_DEFINED_DEGREES,
                EXPECTED_NOT_DEFINED_MINUTES,
                EXPECTED_NOT_DEFINED_MINUTE_SHARE,
                EXPECTED_NOT_DEFINED_LATITUDE_TYPE
        );
        assertEquals(expected, actual);
    }

    @Test
    public void longitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Longitude actual = givenParser.parseLongitude();
        final Longitude expected = new Longitude(37, 39, 6834, EAST);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedLongitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;NA;NA;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Longitude actual = givenParser.parseLongitude();
        final Longitude expected = new Longitude(
                EXPECTED_NOT_DEFINED_DEGREES,
                EXPECTED_NOT_DEFINED_MINUTES,
                EXPECTED_NOT_DEFINED_MINUTE_SHARE,
                EXPECTED_NOT_DEFINED_LONGITUDE_TYPE
        );
        assertEquals(expected, actual);
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Double> optionalActual = givenParser.parseSpeed();
        assertTrue(optionalActual.isPresent());
        final Double actual = optionalActual.get();
        final Double expected = 100.;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Double> optionalActual = givenParser.parseSpeed();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseCourse();
        assertTrue(optionalActual.isPresent());
        final Integer actual = optionalActual.get();
        final Integer expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseCourse();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseAltitude();
        assertTrue(optionalActual.isPresent());
        final Integer actual = optionalActual.get();
        final Integer expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void altitudeShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseAltitude();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void amountOfSatellitesShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseAmountOfSatellites();
        assertTrue(optionalActual.isPresent());
        final Integer actual = optionalActual.get();
        final Integer expected = 177;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAmountOfSatellitesShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseAmountOfSatellites();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Double> optionalActual = givenParser.parseHdop();
        assertTrue(optionalActual.isPresent());
        final Double actual = optionalActual.get();
        final Double expected = 545.4554;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedHdopShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;NA;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Double> optionalActual = givenParser.parseHdop();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void inputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseInputs();
        assertTrue(optionalActual.isPresent());
        final Integer actual = optionalActual.get();
        final Integer expected = 17;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedInputsShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;NA;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseInputs();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void outputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseOutputs();
        assertTrue(optionalActual.isPresent());
        final Integer actual = optionalActual.get();
        final Integer expected = 18;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedOutputsShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;NA;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Integer> optionalActual = givenParser.parseOutputs();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void analogInputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<double[]> optionalActual = givenParser.parseAnalogInputs();
        assertTrue(optionalActual.isPresent());
        final double[] actual = optionalActual.get();
        final double[] expected = {5.5, 4343.454544334, 454.433, 1};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedAnalogInputsShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "NA;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<double[]> optionalActual = givenParser.parseAnalogInputs();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void emptyAnalogInputsShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + ";"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<double[]> optionalActual = givenParser.parseAnalogInputs();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void driverKeyCodeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        final String expected = "keydrivercode";
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDriverKeyCodeShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "NA;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void emptyDriverKeyCodeShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + ";"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Set<Parameter>> optionalActual = givenParser.parseParameters();
        assertTrue(optionalActual.isPresent());
        final Set<Parameter> actual = optionalActual.get();
        final Set<Parameter> expected = Set.of(
                Parameter.builder()
                        .name("param-name-1")
                        .type(INTEGER)
                        .value("654321")
                        .build(),
                Parameter.builder()
                        .name("param-name-2")
                        .type(DOUBLE)
                        .value("65.4321")
                        .build(),
                Parameter.builder()
                        .name("param-name-3")
                        .type(STRING)
                        .value("param-value")
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void emptyParametersShouldNotBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;";
        final WialonMessageComponentParser givenParser = new WialonMessageComponentParser(givenMessage);

        final Optional<Set<Parameter>> optionalActual = givenParser.parseParameters();
        assertTrue(optionalActual.isEmpty());
    }
}
