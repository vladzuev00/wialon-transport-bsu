//package by.bsu.wialontransport.kafka.producer;
//
//import by.bsu.wialontransport.base.AbstractContextTest;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.crud.entity.ParameterEntity;
//import by.bsu.wialontransport.kafka.transportable.data.TransportableData;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
//import static java.time.ZoneOffset.UTC;
//import static org.junit.Assert.assertEquals;
//
//public final class KafkaInboundDataProducerTest extends AbstractContextTest {
//
//    @Autowired
//    private KafkaInboundDataProducer producer;
//
//    @Test
//    public void dataShouldBeMappedToTransportable() {
//        final LocalDateTime givenDateTime = LocalDateTime.of(
//                2022, 11, 15, 14, 56, 43);
//        final int givenLatitudeDegrees = 55;
//        final int givenLatitudeMinutes = 44;
//        final int givenLatitudeMinuteShare = 6025;
//        final DataEntity.Latitude.Type givenLatitudeType = NORTH;
//        final int givenLongitudeDegrees = 37;
//        final int givenLongitudeMinutes = 39;
//        final int givenLongitudeMinuteShare = 6834;
//        final DataEntity.Longitude.Type givenLongitudeType = EAST;
//        final int givenSpeed = 100;
//        final int givenCourse = 15;
//        final int givenAltitude = 10;
//        final int givenAmountOfSatellites = 177;
//        final double givenReductionPrecision = 545.4554;
//        final int givenInputs = 17;
//        final int givenOutputs = 18;
//        final String givenKeyDriverCode = "keydrivercode";
//        final Long givenTrackerId = 256L;
//        final Data givenData = Data.builder()
//                .date(givenDateTime.toLocalDate())
//                .time(givenDateTime.toLocalTime())
//                .latitude(
//                        createLatitude(
//                                givenLatitudeDegrees,
//                                givenLatitudeMinutes,
//                                givenLatitudeMinuteShare,
//                                givenLatitudeType
//                        )
//                )
//                .longitude(
//                        createLongitude(
//                                givenLongitudeDegrees,
//                                givenLongitudeMinutes,
//                                givenLongitudeMinuteShare,
//                                givenLongitudeType
//                        )
//                )
//                .speed(givenSpeed)
//                .course(givenCourse)
//                .altitude(givenAltitude)
//                .amountOfSatellites(givenAmountOfSatellites)
//                .reductionPrecision(givenReductionPrecision)
//                .inputs(givenInputs)
//                .outputs(givenOutputs)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode(givenKeyDriverCode)
//                .parametersByNames(createLinkedHashMapParametersByNames(
//                        "param-name-1", createParameter("param-name-1", INTEGER, "654321"),
//                        "param-name-2", createParameter("param-name-2", DOUBLE, "65.4321"),
//                        "param-name-3", createParameter("param-name-3", STRING, "param-value")))
//                .tracker(createTracker(givenTrackerId))
//                .build();
//
//        final TransportableData actual = this.producer.mapToTransportable(givenData);
//        final TransportableData expected = TransportableData.builder()
//                .epochSeconds(givenDateTime.toEpochSecond(UTC))
//                .latitudeDegrees(givenLatitudeDegrees)
//                .latitudeMinutes(givenLatitudeMinutes)
//                .latitudeMinuteShare(givenLatitudeMinuteShare)
//                .latitudeTypeValue(givenLatitudeType.getValue())
//                .longitudeDegrees(givenLongitudeDegrees)
//                .longitudeMinutes(givenLongitudeMinutes)
//                .longitudeMinuteShare(givenLongitudeMinuteShare)
//                .longitudeTypeValue(givenLongitudeType.getValue())
//                .speed(givenSpeed)
//                .course(givenCourse)
//                .altitude(givenAltitude)
//                .amountOfSatellites(givenAmountOfSatellites)
//                .reductionPrecision(givenReductionPrecision)
//                .inputs(givenInputs)
//                .outputs(givenOutputs)
//                .driverKeyCode(givenKeyDriverCode)
//                .serializedAnalogInputs("5.5,4343.454544334,454.433,1.0")
//                .serializedParameters("param-name-1:1:654321,param-name-2:2:65.4321,param-name-3:3:param-value")
//                .trackerId(givenTrackerId)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    private static Data.Latitude createLatitude(final int degrees, final int minutes, final int minuteShare,
//                                                final DataEntity.Latitude.Type type) {
//        return Data.Latitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static Data.Longitude createLongitude(final int degrees, final int minutes, final int minuteShare,
//                                                  final DataEntity.Longitude.Type type) {
//        return Data.Longitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static Parameter createParameter(final String name, final ParameterEntity.Type type, final String value) {
//        return Parameter.builder()
//                .name(name)
//                .type(type)
//                .value(value)
//                .build();
//    }
//
//    private static Tracker createTracker(final Long id) {
//        return Tracker.builder()
//                .id(id)
//                .build();
//    }
//
//    private static Map<String, Parameter> createLinkedHashMapParametersByNames(final String firstParameterName,
//                                                                               final Parameter firstParameter,
//                                                                               final String secondParameterName,
//                                                                               final Parameter secondParameter,
//                                                                               final String thirdParameterName,
//                                                                               final Parameter thirdParameter) {
//        return new LinkedHashMap<>() {
//            {
//                super.put(firstParameterName, firstParameter);
//                super.put(secondParameterName, secondParameter);
//                super.put(thirdParameterName, thirdParameter);
//            }
//        };
//    }
//}
