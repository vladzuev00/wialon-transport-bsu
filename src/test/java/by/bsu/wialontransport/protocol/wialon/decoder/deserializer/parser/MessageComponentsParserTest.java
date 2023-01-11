package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;

public final class MessageComponentsParserTest {
    private static final String FIELD_NAME_MESSAGE_REGEX = "MESSAGE_REGEX";

    private final String messageRegex;

    public MessageComponentsParserTest()
            throws Exception {
        this.messageRegex = findMessageRegex();
    }

    @Test
    public void parserShouldBeCreated() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        new MessageComponentsParser(givenMessage);
    }

    @Test(expected = NotValidMessageException.class)
    public void parserShouldNotBeCreatedWithNotValidMessage() {
        final String givenMessage = "not valid message";
        new MessageComponentsParser(givenMessage);
    }

    @Test
    public void messageShouldMatchToRegex() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        assertTrue(givenMessage.matches(this.messageRegex));
    }

    @Test
    public void messageWithNotDefinedComponentsShouldMatchToRegex() {
        final String givenMessage = "NA;NA;5544.6025;N;03739.6834;E;NA;NA;NA;NA;NA;NA;NA;;NA;";
        assertTrue(givenMessage.matches(this.messageRegex));
    }

    private static String findMessageRegex()
            throws Exception {
        final Field messageRegexField = MessageComponentsParser.class.getDeclaredField(FIELD_NAME_MESSAGE_REGEX);
        messageRegexField.setAccessible(true);
        try {
            return (String) messageRegexField.get(null);
        } finally {
            messageRegexField.setAccessible(false);
        }
    }
}
