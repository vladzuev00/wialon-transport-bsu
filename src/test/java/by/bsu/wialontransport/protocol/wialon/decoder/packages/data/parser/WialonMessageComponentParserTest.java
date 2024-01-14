package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType.EAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
}
