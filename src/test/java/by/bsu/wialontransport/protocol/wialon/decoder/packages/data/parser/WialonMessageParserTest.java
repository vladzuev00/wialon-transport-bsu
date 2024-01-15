package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType.EAST;
import static org.junit.Assert.assertEquals;

public final class WialonMessageParserTest {
    private final WialonMessageParser parser = new WialonMessageParser();

    @Test
    public void messageWithAllDefinedComponentsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value"
                + "|"
                + "151123;145644;5545.6025;N;03739.6934;E;101;16;11;178;545.4555;18;19;"
                + "5.6,4344.454544334,455.433,2;"
                + "second-code;"
                + "param-name-4:1:654322,param-name-5:2:66.4321,param-name-6:3:param-value-2";

        final List<WialonData> actual = parser.parse(givenMessage);
        final List<WialonData> expected = List.of(
                WialonData.builder()
                        .date(LocalDate.of(2022, 11, 15))
                        .time(LocalTime.of(14, 56, 43))
                        .latitude(new Latitude(55, 44, 6025, NORTH))
                        .longitude(new Longitude(37, 39, 6834, EAST))
                        .speed(100.)
                        .course(15)
                        .altitude(10)
                        .amountOfSatellites(177)
                        .hdop(545.4554)
                        .inputs(17)
                        .outputs(18)
                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                        .driverKeyCode("keydrivercode")
                        .parameters(
                                Set.of(
                                        createParameter("param-name-1", INTEGER, "654321"),
                                        createParameter("param-name-2", DOUBLE, "65.4321"),
                                        createParameter("param-name-3", STRING, "param-value")
                                )
                        )
                        .build(),
                WialonData.builder()
                        .date(LocalDate.of(2023, 11, 15))
                        .time(LocalTime.of(14, 56, 44))
                        .latitude(new Latitude(55, 45, 6025, NORTH))
                        .longitude(new Longitude(37, 39, 6934, EAST))
                        .speed(101.)
                        .course(16)
                        .altitude(11)
                        .amountOfSatellites(178)
                        .hdop(545.4555)
                        .inputs(18)
                        .outputs(19)
                        .analogInputs(new double[]{5.6, 4344.454544334, 455.433, 2})
                        .driverKeyCode("second-code")
                        .parameters(
                                Set.of(
                                        createParameter("param-name-4", INTEGER, "654322"),
                                        createParameter("param-name-5", DOUBLE, "66.4321"),
                                        createParameter("param-name-6", STRING, "param-value-2")
                                )
                        )
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void messageWithNotDefinedNotRequiredComponentsShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;NA;NA;NA;NA;NA;NA;NA;"
                + "NA;"
                + "NA;"
                + "|"
                + "151123;145644;5545.6025;N;03739.6934;E;NA;NA;NA;NA;NA;NA;NA;"
                + "NA;"
                + "NA;";

        final List<WialonData> actual = parser.parse(givenMessage);
        final List<WialonData> expected = List.of(
                WialonData.builder()
                        .date(LocalDate.of(2022, 11, 15))
                        .time(LocalTime.of(14, 56, 43))
                        .latitude(new Latitude(55, 44, 6025, NORTH))
                        .longitude(new Longitude(37, 39, 6834, EAST))
                        .build(),
                WialonData.builder()
                        .date(LocalDate.of(2023, 11, 15))
                        .time(LocalTime.of(14, 56, 44))
                        .latitude(new Latitude(55, 45, 6025, NORTH))
                        .longitude(new Longitude(37, 39, 6934, EAST))
                        .build()
        );
        assertEquals(expected, actual);
    }

    private static Parameter createParameter(final String name, final Type type, final String value) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .build();
    }
}
