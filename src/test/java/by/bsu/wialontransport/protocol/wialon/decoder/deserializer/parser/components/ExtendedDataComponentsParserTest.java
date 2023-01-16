package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.exception.NoCallMatchMethodBeforeParsingException;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static org.junit.Assert.*;

public final class ExtendedDataComponentsParserTest {
    private final ExtendedDataComponentsParser parser;

    public ExtendedDataComponentsParserTest() {
        this.parser = new ExtendedDataComponentsParser();
    }

    @Test
    public void messageShouldMatchToRegex() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        assertTrue(this.parser.match(givenMessage));
    }

    @Test
    public void messageWithNotDefinedComponentsShouldMatchToRegex() {
        final String givenMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;;";
        assertTrue(this.parser.match(givenMessage));
    }

    @Test
    public void messageWithNotDefinedComponentsAndNaAsAnalogInputsShouldMatchToRegex() {
        final String givenMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;";
        assertTrue(this.parser.match(givenMessage));
    }

    @Test
    public void messageShouldNotMatchToRegex() {
        final String givenMessage = "not valid";
        assertFalse(this.parser.match(givenMessage));
    }

    @Test
    public void messageOfDataShouldNotMatchToRegex() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";
        assertFalse(this.parser.match(givenMessage));
    }

    @Test
    public void reductionPrecisionShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final double actual = this.parser.parseReductionPrecision();
        final double expected = 545.4554;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedReductionPrecisionShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;NA;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final double actual = this.parser.parseReductionPrecision();
        final double expected = Double.MIN_VALUE;
        assertEquals(expected, actual, 0.);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void reductionPrecisionShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final ExtendedDataComponentsParser parser = new ExtendedDataComponentsParser();
        parser.parseReductionPrecision();
    }

    @Test
    public void inputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseInputs();
        final int expected = 17;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedInputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;NA;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseInputs();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void inputsShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final ExtendedDataComponentsParser parser = new ExtendedDataComponentsParser();
        parser.parseInputs();
    }

    @Test
    public void outputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseOutputs();
        final int expected = 18;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedOutputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;NA;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseOutputs();
        final int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void outputsShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final ExtendedDataComponentsParser parser = new ExtendedDataComponentsParser();
        parser.parseOutputs();
    }

    @Test
    public void analogInputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final double[] actual = this.parser.parseAnalogInputs();
        final double[] expected = {5.5, 4343.454544334, 454.433, 1};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedAnalogInputsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + ";"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final double[] actual = this.parser.parseAnalogInputs();
        final double[] expected = {};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedAnalogInputsAsNaShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "NA;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final double[] actual = this.parser.parseAnalogInputs();
        final double[] expected = {};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void analogInputsShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final ExtendedDataComponentsParser parser = new ExtendedDataComponentsParser();
        parser.parseAnalogInputs();
    }

    @Test
    public void driverKeyCodeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final String actual = this.parser.parseDriverKeyCode();
        final String expected = "keydrivercode";
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDriverCodeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "NA;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final String actual = this.parser.parseDriverKeyCode();
        final String expected = "not defined";
        assertEquals(expected, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void driverKeyCodeShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final ExtendedDataComponentsParser parser = new ExtendedDataComponentsParser();
        parser.parseDriverKeyCode();
    }

    @Test
    public void parametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";

        assertTrue(this.parser.match(givenMessage));

        final List<Parameter> actual = this.parser.parseParameters();
        final List<Parameter> expected = List.of(
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
                        .build());
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedParametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;17;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;";

        assertTrue(this.parser.match(givenMessage));

        final List<Parameter> actual = this.parser.parseParameters();
        assertTrue(actual.isEmpty());
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void parametersShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final ExtendedDataComponentsParser parser = new ExtendedDataComponentsParser();
        parser.parseParameters();
    }
}
