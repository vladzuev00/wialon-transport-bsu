package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.bsu.wialontransport.crud.dto.ExtendedData.extendedDataBuilder;
import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static org.junit.Assert.assertEquals;

public final class ExtendedDataParserTest extends AbstractContextTest {

    @Autowired
    private ExtendedDataParser parser;

    @Test
    public void extendedDataShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";

        final ExtendedData actual = this.parser.parse(givenMessage);
        final ExtendedData expected = extendedDataBuilder()
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
                .reductionPrecision(545.4554)
                .inputs(17)
                .outputs(18)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .parameters(List.of(
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
                                .build()))
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = NotValidMessageException.class)
    public void notValidExtendedDataShouldNotBeParsed() {
        //not valid date
        final String givenMessage = "1511232;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
        this.parser.parse(givenMessage);
    }
}
