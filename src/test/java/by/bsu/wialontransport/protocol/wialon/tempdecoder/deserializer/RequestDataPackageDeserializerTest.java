//package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer;
//
//import by.bsu.wialontransport.base.AbstractContextTest;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Data.Latitude;
//import by.bsu.wialontransport.crud.dto.Data.Longitude;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
//import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
//import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Map;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
//import static org.junit.Assert.assertEquals;
//
//public final class RequestDataPackageDeserializerTest extends AbstractContextTest {
//
//    @Autowired
//    private RequestDataPackageDeserializer deserializer;
//
//    @Test
//    public void requestDataPackageShouldBeDeserialized() {
//        final String givenSource = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value";
//
//        final WialonPackage actual = this.deserializer.deserialize(givenSource);
//        final WialonRequestDataPackage expected = new WialonRequestDataPackage(Data.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 43))
//                .latitude(Latitude.builder()
//                        .degrees(55)
//                        .minutes(44)
//                        .minuteShare(6025)
//                        .type(NORTH)
//                        .build())
//                .longitude(Longitude.builder()
//                        .degrees(37)
//                        .minutes(39)
//                        .minuteShare(6834)
//                        .type(EAST)
//                        .build())
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parametersByNames(
//                        Map.of(
//                                "param-name-1", createParameter("param-name-1", INTEGER, "654321"),
//                                "param-name-2", createParameter("param-name-2", DOUBLE, "65.4321"),
//                                "param-name-3", createParameter("param-name-3", STRING, "param-value")
//                        )
//                )
//                .build()
//        );
//        assertEquals(expected, actual);
//    }
//
//    private static Parameter createParameter(final String name, final Type type, final String value) {
//        return Parameter.builder()
//                .name(name)
//                .type(type)
//                .value(value)
//                .build();
//    }
//}
