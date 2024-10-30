package by.bsu.wialontransport.protocol.wialon.decoder.location.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;

import static by.bsu.wialontransport.model.ParameterType.*;
import static org.junit.jupiter.api.Assertions.*;

public final class WialonLocationComponentParserTest {

    @Test
    public void parserShouldBeCreated() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";

        new WialonLocationComponentParser(givenSource);
    }

    @Test
    public void parserShouldBeCreatedBySourceWithNotDefinedComponents() {
        final String givenSource = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;;";

        new WialonLocationComponentParser(givenSource);
    }

    @Test
    public void parserShouldBeCreatedBySourceWithNotDefinedComponentsAndWithNAAsAnalogInputs() {
        final String givenSource = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;";

        new WialonLocationComponentParser(givenSource);
    }

    @Test
    public void parserShouldNotBeCreatedByNotValidSource() {
        final String givenSource = "not valid";

        assertThrows(IllegalArgumentException.class, () -> new WialonLocationComponentParser(givenSource));
    }

    @Test
    public void parserShouldNotBeCreatedBecauseOfThereIsNoParameterDelimiter() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321param-name-2:2:65.4321,param-name-3:3:param-value";

        assertThrows(IllegalArgumentException.class, () -> new WialonLocationComponentParser(givenSource));
    }

    @Test
    public void dateShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<LocalDate> optionalActual = givenParser.parseDate();
        assertTrue(optionalActual.isPresent());
        final LocalDate actual = optionalActual.get();
        final LocalDate expected = LocalDate.of(2022, 11, 15);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateShouldBeParsed() {
        final String givenSource = "NA;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<LocalDate> optionalActual = givenParser.parseDate();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void timeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<LocalTime> optionalActual = givenParser.parseTime();
        assertTrue(optionalActual.isPresent());
        final LocalTime actual = optionalActual.get();
        final LocalTime expected = LocalTime.of(14, 56, 43);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedTimeShouldBeParsed() {
        final String givenSource = "151122;NA;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<LocalTime> optionalActual = givenParser.parseTime();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void latitudeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseLatitude();
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = -57.40694444444445;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedLatitudeShouldBeParsed() {
        final String givenSource = "151122;145643;NA;NA;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseLatitude();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void longitudeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseLongitude();
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = 39.54833333333333;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedLongitudeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;NA;NA;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseLongitude();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseSpeed();
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = 100;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseSpeed();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseCourse();
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        final int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseCourse();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseAltitude();
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        final int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAltitudeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseAltitude();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void satelliteCountShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseSatelliteCount();
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        final int expected = 177;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSatelliteCountShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseSatelliteCount();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseHdop();
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        final double expected = 545.4554;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedHdopShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;NA;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalDouble optionalActual = givenParser.parseHdop();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void inputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseInputs();
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        final int expected = 17;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedInputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;NA;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseInputs();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void outputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseOutputs();
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        final int expected = 18;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedOutputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;NA;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final OptionalInt optionalActual = givenParser.parseOutputs();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void analogInputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final double[] actual = givenParser.parseAnalogInputs();
        final double[] expected = {5.5, 4343.454544334, 454.433, 1};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void emptyAnalogInputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final double[] actual = givenParser.parseAnalogInputs();
        final double[] expected = {};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedAnalogInputsShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;NA;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final double[] actual = givenParser.parseAnalogInputs();
        final double[] expected = {};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void driverKeyCodeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        final String expected = "keydrivercode";
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDriverKeyCodeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;NA;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void emptyDriverKeyCodeShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Optional<String> optionalActual = givenParser.parseDriverKeyCode();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Set<Parameter> actual = givenParser.parseParameters();
        final Set<Parameter> expected = Set.of(
                Parameter.builder().name("param-name-1").type(INTEGER).value("654321").build(),
                Parameter.builder().name("param-name-2").type(DOUBLE).value("65.4321").build(),
                Parameter.builder().name("param-name-3").type(STRING).value("param-value").build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void emptyParametersShouldBeParsed() {
        final String givenSource = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;";
        final WialonLocationComponentParser givenParser = new WialonLocationComponentParser(givenSource);

        final Set<Parameter> actual = givenParser.parseParameters();
        assertTrue(actual.isEmpty());
    }
}
