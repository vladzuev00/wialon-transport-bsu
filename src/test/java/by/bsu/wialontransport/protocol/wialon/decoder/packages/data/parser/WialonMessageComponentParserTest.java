package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidMessageException;
import org.junit.Test;

public final class WialonMessageComponentParserTest {

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

}
