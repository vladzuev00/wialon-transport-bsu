package by.bsu.wialontransport.protocol.wialon.decoder.data.parser;

import by.bsu.wialontransport.protocol.wialon.decoder.data.parser.WialonSubMessageComponentParser.LatitudeParser;
import by.bsu.wialontransport.protocol.wialon.decoder.data.parser.WialonSubMessageComponentParser.LongitudeParser;
import by.bsu.wialontransport.protocol.wialon.decoder.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.decoder.data.parser.WialonSubMessageComponentParser.GeographicCoordinateParser.*;
import static by.bsu.wialontransport.protocol.wialon.decoder.data.parser.WialonSubMessageComponentParser.NOT_DEFINED_DATE;
import static by.bsu.wialontransport.protocol.wialon.decoder.data.parser.WialonSubMessageComponentParser.NOT_DEFINED_TIME;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere.EAST;
import static org.junit.Assert.*;

public final class WialonSubMessageComponentParserTest {

    @Test
    public void parserShouldBeCreated() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";

        new WialonSubMessageComponentParser(givenSubMessage);
    }

    @Test
    public void parserShouldBeCreatedBySubMessageWithNotDefinedComponents() {
        final String givenSubMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;;";

        new WialonSubMessageComponentParser(givenSubMessage);
    }

    @Test
    public void parserShouldBeCreatedBySubMessageWithNotDefinedComponentsAndWithNAAsAnalogInputs() {
        final String givenSubMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;";

        new WialonSubMessageComponentParser(givenSubMessage);
    }

    @Test(expected = NotValidSubMessageException.class)
    public void parserShouldNotBeCreatedByNotValidSubMessage() {
        final String givenSubMessage = "not valid";

        new WialonSubMessageComponentParser(givenSubMessage);
    }

    @Test(expected = NotValidSubMessageException.class)
    public void parserShouldNotBeCreatedBecauseOfThereIsNoParametersDelimiter() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321param-name-2:2:65.4321,param-name-3:3:param-value";

        new WialonSubMessageComponentParser(givenSubMessage);
    }

    @Test
    public void dateShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final LocalDate actual = givenParser.parseDate();
        final LocalDate expected = LocalDate.of(2022, 11, 15);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateShouldBeParsed() {
        final String givenSubMessage = "NA;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final LocalDate actual = givenParser.parseDate();
        assertSame(NOT_DEFINED_DATE, actual);
    }

    @Test
    public void timeShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final LocalTime actual = givenParser.parseTime();
        final LocalTime expected = LocalTime.of(14, 56, 43);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedTimeShouldBeParsed() {
        final String givenSubMessage = "151122;NA;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final LocalTime actual = givenParser.parseTime();
        assertSame(NOT_DEFINED_TIME, actual);
    }

    @Test
    public void latitudeShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Latitude actual = givenParser.parseLatitude();
        final Latitude expected = new Latitude(55, 44, 6025, NORTH);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedLatitudeShouldBeParsed() {
        final String givenSubMessage = "151122;145643;NA;NA;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Latitude actual = givenParser.parseLatitude();
        final Latitude expected = new Latitude(
                NOT_DEFINED_DEGREES,
                NOT_DEFINED_MINUTES,
                NOT_DEFINED_MINUTE_SHARE,
                LatitudeParser.NOT_DEFINED_TYPE
        );
        assertEquals(expected, actual);
    }

    @Test
    public void longitudeShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Longitude actual = givenParser.parseLongitude();
        final Longitude expected = new Longitude(37, 39, 6834, EAST);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedLongitudeShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;NA;NA;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Longitude actual = givenParser.parseLongitude();
        final Longitude expected = new Longitude(
                NOT_DEFINED_DEGREES,
                NOT_DEFINED_MINUTES,
                NOT_DEFINED_MINUTE_SHARE,
                LongitudeParser.NOT_DEFINED_TYPE
        );
        assertEquals(expected, actual);
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Double> optionalActual = givenParser.parseSpeed();
        assertTrue(optionalActual.isPresent());

        final Double actual = optionalActual.get();
        final Double expected = 100.;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldNotBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Double> optionalActual = givenParser.parseSpeed();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Integer> optionalActual = givenParser.parseCourse();
        assertTrue(optionalActual.isPresent());

        final Integer actual = optionalActual.get();
        final Integer expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldNotBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Integer> optionalActual = givenParser.parseCourse();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Integer> optionalActual = givenParser.parseAltitude();
        assertTrue(optionalActual.isPresent());

        final Integer actual = optionalActual.get();
        final Integer expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void altitudeShouldNotBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Integer> optionalActual = givenParser.parseAltitude();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void amountOfSatellitesShouldBeParsed() {
        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);

        final Optional<Integer> optionalActual = givenParser.parseAmountOfSatellites();
        assertTrue(optionalActual.isPresent());

        final Integer actual = optionalActual.get();
        final Integer expected = 177;
        assertEquals(expected, actual);
    }

//    @Test
//    public void notDefinedAmountOfSatellitesShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Integer> optionalActual = givenParser.parseAmountOfSatellites();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void hdopShouldBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Double> optionalActual = givenParser.parseHdop();
//        assertTrue(optionalActual.isPresent());
//        final Double actual = optionalActual.get();
//        final Double expected = 545.4554;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void notDefinedHdopShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;NA;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Double> optionalActual = givenParser.parseHdop();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void inputsShouldBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Integer> optionalActual = givenParser.parseInputs();
//        assertTrue(optionalActual.isPresent());
//        final Integer actual = optionalActual.get();
//        final Integer expected = 17;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void notDefinedInputsShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;NA;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Integer> optionalActual = givenParser.parseInputs();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void outputsShouldBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Integer> optionalActual = givenParser.parseOutputs();
//        assertTrue(optionalActual.isPresent());
//        final Integer actual = optionalActual.get();
//        final Integer expected = 18;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void notDefinedOutputsShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;NA;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Integer> optionalActual = givenParser.parseOutputs();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void analogInputsShouldBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<double[]> optionalActual = givenParser.parseAnalogInputs();
//        assertTrue(optionalActual.isPresent());
//        final double[] actual = optionalActual.get();
//        final double[] expected = {5.5, 4343.454544334, 454.433, 1};
//        assertArrayEquals(expected, actual, 0.);
//    }
//
//    @Test
//    public void notDefinedAnalogInputsShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "NA;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<double[]> optionalActual = givenParser.parseAnalogInputs();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void emptyAnalogInputsShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + ";"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<double[]> optionalActual = givenParser.parseAnalogInputs();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void driverKeyCodeShouldBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
//        assertTrue(optionalActual.isPresent());
//        final String actual = optionalActual.get();
//        final String expected = "keydrivercode";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void notDefinedDriverKeyCodeShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "NA;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void emptyDriverKeyCodeShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + ";"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void parametersShouldBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Set<Parameter>> optionalActual = givenParser.parseParameters();
//        assertTrue(optionalActual.isPresent());
//        final Set<Parameter> actual = optionalActual.get();
//        final Set<Parameter> expected = Set.of(
//                Parameter.builder()
//                        .name("param-name-1")
//                        .type(INTEGER)
//                        .value("654321")
//                        .build(),
//                Parameter.builder()
//                        .name("param-name-2")
//                        .type(DOUBLE)
//                        .value("65.4321")
//                        .build(),
//                Parameter.builder()
//                        .name("param-name-3")
//                        .type(STRING)
//                        .value("param-value")
//                        .build()
//        );
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void emptyParametersShouldNotBeParsed() {
//        final String givenSubMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;";
//        final WialonSubMessageComponentParser givenParser = new WialonSubMessageComponentParser(givenSubMessage);
//
//        final Optional<Set<Parameter>> optionalActual = givenParser.parseParameters();
//        assertTrue(optionalActual.isEmpty());
//    }
}
