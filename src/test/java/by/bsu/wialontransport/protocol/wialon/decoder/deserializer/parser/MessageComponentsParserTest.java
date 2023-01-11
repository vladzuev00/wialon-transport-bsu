package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static java.lang.Integer.MIN_VALUE;
import static java.time.LocalDateTime.MIN;
import static org.junit.Assert.assertEquals;
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
        final String givenMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;NA;";
        assertTrue(givenMessage.matches(this.messageRegex));
    }

    @Test
    public void dateTimeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final LocalDateTime actual = parser.parseDateTime();
        final LocalDateTime expected = LocalDateTime.of(
                2022, 11, 15, 14, 56, 43);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateTimeShouldBeParsed() {
        final String givenMessage = "NA;NA;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final LocalDateTime actual = parser.parseDateTime();
        assertEquals(MIN, actual);
    }

    @Test
    public void latitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final Latitude actual = parser.parseLatitude();
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
        final String givenMessage = "151122;145643;NA;NA;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final Latitude actual = parser.parseLatitude();
        final Latitude expected = Latitude.builder()
                .degrees(MIN_VALUE)
                .minutes(MIN_VALUE)
                .minuteShare(MIN_VALUE)
                .type(DataEntity.Latitude.Type.NOT_DEFINED)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void longitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final Longitude actual = parser.parseLongitude();
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
        final String givenMessage = "151122;145643;5544.6025;N;NA;NA;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final Longitude actual = parser.parseLongitude();
        final Longitude expected = Longitude.builder()
                .degrees(MIN_VALUE)
                .minutes(MIN_VALUE)
                .minuteShare(MIN_VALUE)
                .type(DataEntity.Longitude.Type.NOT_DEFINED)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseSpeed();
        final int expected = 100;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseSpeed();
        assertEquals(MIN_VALUE, actual);
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseCourse();
        final int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseCourse();
        assertEquals(MIN_VALUE, actual);
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAltitude();
        final int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAltitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAltitude();
        assertEquals(MIN_VALUE, actual);
    }

    @Test
    public void amountSatellitesShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAmountSatellites();
        final int expected = 177;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAmountSatellitesShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAmountSatellites();
        assertEquals(MIN_VALUE, actual);
    }

    @Test
    public void parametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name1:1:654321,param-name2:2:65.4321,param-name3:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final List<Parameter> actual = parser.parseParameters();
        final List<Parameter> expected = List.of(
                Parameter.builder()
                        .name("param-name1")
                        .type(INTEGER)
                        .value("654321")
                        .build(),
                Parameter.builder()
                        .name("param-name2")
                        .type(DOUBLE)
                        .value("65.4321")
                        .build(),
                Parameter.builder()
                        .name("param-name3")
                        .type(STRING)
                        .value("param-value")
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void emptyParametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final List<Parameter> actual = parser.parseParameters();
        assertTrue(actual.isEmpty());
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
