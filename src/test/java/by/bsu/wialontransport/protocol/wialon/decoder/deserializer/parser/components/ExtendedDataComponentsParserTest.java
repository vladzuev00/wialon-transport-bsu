package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
