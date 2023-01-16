package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.crud.dto.Data.dataBuilder;
import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;

public final class DataParserTest extends AbstractContextTest {

    @Autowired
    private DataParser parser;

    @Test
    public void dataShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177";

        final Data actual = this.parser.parse(givenMessage);
        final Data expected = dataBuilder()
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(14, 56, 43))
                .latitude(Latitude.builder()
                        .degrees(55)
                        .minutes(44)
                        .minuteShare(6025)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(37)
                        .minutes(39)
                        .minuteShare(6834)
                        .type(EAST)
                        .build())
                .speed(100)
                .course(15)
                .height(10)
                .amountOfSatellites(177)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = NotValidMessageException.class)
    public void notValidDataShouldNotBeParsed() {
        //not valid time
        final String givenMessage = "151122;1456434;5544.6025;N;03739.6834;E;100;15;10;177";
        this.parser.parse(givenMessage);
    }
}
