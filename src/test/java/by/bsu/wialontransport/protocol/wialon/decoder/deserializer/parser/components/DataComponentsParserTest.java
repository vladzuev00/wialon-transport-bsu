package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components;

import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.exception.NoCallMatchMethodBeforeParsingException;
import org.junit.Test;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static java.time.LocalDateTime.MIN;
import static org.junit.Assert.*;

public final class DataComponentsParserTest {
    private final DataComponentsParser parser;

    public DataComponentsParserTest() {
        this.parser = new DataComponentsParser();
    }

    @Test
    public void messageShouldMatchToRegex() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";
        assertTrue(this.parser.match(givenMessage));
    }

    @Test
    public void messageWithNotDefinedComponentsShouldMatchToRegex() {
        final String givenMessage = "NA;NA;NA;NA;NA;NA;NA;NA;NA;NA";
        assertTrue(this.parser.match(givenMessage));
    }

    @Test
    public void messageShouldNotMatchToRegex() {
        final String givenMessage = "not valid";
        assertFalse(this.parser.match(givenMessage));
    }

    @Test
    public void messageOfExtendedDataShouldNotMatchToRegex() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        assertFalse(this.parser.match(givenMessage));
    }

    @Test
    public void dateTimeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final LocalDateTime actual = this.parser.parseDateTime();
        final LocalDateTime expected = LocalDateTime.of(
                2022, 11, 15, 14, 56, 43);
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateTimeShouldBeParsed() {
        final String givenMessage = "NA;NA;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final LocalDateTime actual = this.parser.parseDateTime();
        assertEquals(MIN, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void dateTimeShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseDateTime();
    }

    @Test
    public void latitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final Latitude actual = this.parser.parseLatitude();
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
        final String givenMessage = "151122;145643;NA;NA;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final Latitude actual = this.parser.parseLatitude();
        final Latitude expected = Latitude.builder()
                .degrees(Integer.MIN_VALUE)
                .minutes(Integer.MIN_VALUE)
                .minuteShare(Integer.MIN_VALUE)
                .type(DataEntity.Latitude.Type.NOT_DEFINED)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void latitudeShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseLatitude();
    }

    @Test
    public void longitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final Longitude actual = this.parser.parseLongitude();
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
        final String givenMessage = "151122;145643;5544.6025;N;NA;NA;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final Longitude actual = this.parser.parseLongitude();
        final Longitude expected = Longitude.builder()
                .degrees(Integer.MIN_VALUE)
                .minutes(Integer.MIN_VALUE)
                .minuteShare(Integer.MIN_VALUE)
                .type(DataEntity.Longitude.Type.NOT_DEFINED)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void longitudeShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseLongitude();
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseSpeed();
        final int expected = 100;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseSpeed();
        assertEquals(Integer.MIN_VALUE, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void speedShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseSpeed();
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseCourse();
        final int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseCourse();
        assertEquals(Integer.MIN_VALUE, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void courseShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseCourse();
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseAltitude();
        final int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAltitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseAltitude();
        assertEquals(Integer.MIN_VALUE, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void altitudeShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseAltitude();
    }

    @Test
    public void amountSatellitesShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseAmountSatellites();
        final int expected = 177;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAmountSatellitesShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA";

        assertTrue(this.parser.match(givenMessage));

        final int actual = this.parser.parseAmountSatellites();
        assertEquals(Integer.MIN_VALUE, actual);
    }

    @Test(expected = NoCallMatchMethodBeforeParsingException.class)
    public void amountSatellitesShouldNotBeParsedBecauseOfNoCallingMatchMethod() {
        final DataComponentsParser parser = new DataComponentsParser();
        parser.parseAmountSatellites();
    }
}
