//package by.bsu.wialontransport.kafka.producer;
//
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Data.Latitude;
//import by.bsu.wialontransport.crud.dto.Data.Longitude;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.crud.entity.ParameterEntity;
//import by.bsu.wialontransport.kafka.transportable.TransportableData;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
//import static by.bsu.wialontransport.kafka.producer.AbstractKafkaDataProducer.*;
//import static org.junit.Assert.assertEquals;
//
//public final class AbstractKafkaDataProducerTest {
//
//    private final AbstractKafkaDataProducer<TransportableData> producer = new TestKafkaDataProducer();
//
//    @Test
//    public void epochSecondsShouldBeFound() {
//        final Data givenData = Data.builder()
//                .date(LocalDate.of(2023, 5, 20))
//                .time(LocalTime.of(10, 11, 12))
//                .build();
//
//        final long actual = findEpochSeconds(givenData);
//        final long expected = 1684577472;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void latitudeDegreesShouldBeFound() {
//        final int givenLatitudeDegrees = 10;
//        final Data givenData = Data.builder()
//                .latitude(Latitude.builder()
//                        .degrees(givenLatitudeDegrees)
//                        .build())
//                .build();
//
//        final int actual = findLatitudeDegrees(givenData);
//        assertEquals(givenLatitudeDegrees, actual);
//    }
//
//    @Test
//    public void latitudeMinutesShouldBeFound() {
//        final int givenLatitudeMinutes = 10;
//        final Data givenData = Data.builder()
//                .latitude(Latitude.builder()
//                        .minutes(givenLatitudeMinutes)
//                        .build())
//                .build();
//
//        final int actual = findLatitudeMinutes(givenData);
//        assertEquals(givenLatitudeMinutes, actual);
//    }
//
//    @Test
//    public void latitudeMinuteShareShouldBeFound() {
//        final int givenLatitudeMinuteShare = 10;
//        final Data giveData = Data.builder()
//                .latitude(Latitude.builder()
//                        .minuteShare(givenLatitudeMinuteShare)
//                        .build())
//                .build();
//
//        final int actual = findLatitudeMinuteShare(giveData);
//        assertEquals(givenLatitudeMinuteShare, actual);
//    }
//
//    @Test
//    public void latitudeTypeValueShouldBeFound() {
//        final DataEntity.Latitude.Type givenType = NORTH;
//        final Data givenData = Data.builder()
//                .latitude(Latitude.builder()
//                        .type(givenType)
//                        .build())
//                .build();
//
//        final char actual = findLatitudeTypeValue(givenData);
//        final char expected = givenType.getValue();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void longitudeDegreesShouldBeFound() {
//        final int givenLongitudeDegrees = 10;
//        final Data givenData = Data.builder()
//                .longitude(Longitude.builder()
//                        .degrees(givenLongitudeDegrees)
//                        .build())
//                .build();
//
//        final int actual = findLongitudeDegrees(givenData);
//        assertEquals(givenLongitudeDegrees, actual);
//    }
//
//    @Test
//    public void longitudeMinutesShouldBeFound() {
//        final int givenLongitudeMinutes = 11;
//        final Data givenData = Data.builder()
//                .longitude(Longitude.builder()
//                        .minutes(givenLongitudeMinutes)
//                        .build())
//                .build();
//
//        final int actual = findLongitudeMinutes(givenData);
//        assertEquals(givenLongitudeMinutes, actual);
//    }
//
//    @Test
//    public void longitudeMinuteShareShouldBeFound() {
//        final int givenLongitudeMinuteShare = 12;
//        final Data givenData = Data.builder()
//                .longitude(Longitude.builder()
//                        .minuteShare(givenLongitudeMinuteShare)
//                        .build())
//                .build();
//
//        final int actual = findLongitudeMinuteShare(givenData);
//        assertEquals(givenLongitudeMinuteShare, actual);
//    }
//
//    @Test
//    public void longitudeTypeValueShouldBeFound() {
//        final DataEntity.Longitude.Type givenType = EAST;
//        final Data givenData = Data.builder()
//                .longitude(Longitude.builder()
//                        .type(givenType)
//                        .build())
//                .build();
//
//        final char actual = findLongitudeTypeValue(givenData);
//        final char expected = givenType.getValue();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void analogInputsShouldBeSerialized() {
//        final Data givenData = Data.builder()
//                .analogInputs(new double[]{1.1, 2.2, 3.3})
//                .build();
//
//        final String actual = this.producer.serializeAnalogInputs(givenData);
//        final String expected = "1.1,2.2,3.3";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void parametersWithoutIdShouldBeSerialized() {
//        final Data givenData = Data.builder()
//                .parametersByNames(
//                        createLinkedHashMapParametersByNames(
//                                "first", createParameter("first", INTEGER, "1"),
//                                "second", createParameter("second", DOUBLE, "1.1"),
//                                "third", createParameter("third", STRING, "text")
//                        )
//                )
//                .build();
//
//        final String actual = this.producer.serializeParameters(givenData);
//        final String expected = "first:1:1,second:2:1.1,third:3:text";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void parametersWithIdShouldBeSerialized() {
//        final Data givenData = Data.builder()
//                .parametersByNames(
//                        createLinkedHashMapParametersByNames(
//                                "first", createParameter(255L,"first", INTEGER, "1"),
//                                "second", createParameter(256L, "second", DOUBLE, "1.1"),
//                                "third", createParameter(257L,"third", STRING, "text")
//                        )
//                )
//                .build();
//
//        final String actual = this.producer.serializeParameters(givenData);
//        final String expected = "255:first:1:1,256:second:2:1.1,257:third:3:text";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void trackerIdShouldBeFound() {
//        final Long givenTrackerId = 255L;
//        final Data givenData = Data.builder()
//                .tracker(createTracker(givenTrackerId))
//                .build();
//
//        final Long actual = findTrackerId(givenData);
//        assertEquals(givenTrackerId, actual);
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
//    private static Parameter createParameter(final Long id,
//                                             final String name,
//                                             final ParameterEntity.Type type,
//                                             final String value) {
//        return Parameter.builder()
//                .id(id)
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
//
//    private static final class TestKafkaDataProducer extends AbstractKafkaDataProducer<TransportableData> {
//
//        public TestKafkaDataProducer() {
//            super(null, null, null);
//        }
//
//        @Override
//        protected TransportableData mapToTransportable(final Data source) {
//            throw new UnsupportedOperationException();
//        }
//    }
//}
