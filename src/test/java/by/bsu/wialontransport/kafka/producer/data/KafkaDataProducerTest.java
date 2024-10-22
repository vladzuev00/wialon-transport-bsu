package by.bsu.wialontransport.kafka.producer.data;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.kafka.model.view.ParameterView;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableData;
import by.bsu.wialontransport.model.Coordinate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;

public final class KafkaDataProducerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TestKafkaDataProducer producer = new TestKafkaDataProducer(objectMapper);

    @Test
    public void dataShouldBeMappedToTransportable() {
        final Location givenData = Location.builder()
                .id(255L)
                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                .coordinate(new Coordinate(5.5, 6.6))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .hdop(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(
                        createParametersByNames(
                                createParameter(256L, "first-param", INTEGER, "1"),
                                createParameter(257L, "second-param", STRING, "string")
                        )
                )
                .tracker(createTracker(258L))
                .build();

        final TransportableData actual = producer.mapToTransportable(givenData);
        final TestTransportableData expected = TestTransportableData.builder()
                .epochSeconds(1673151495)
                .latitude(5.5)
                .longitude(6.6)
                .course(27)
                .speed(26)
                .altitude(28)
                .amountOfSatellites(29)
                .hdop(30.5)
                .inputs(31)
                .outputs(32)
                .serializedAnalogInputs("[0.2,0.3,0.4]")
                .driverKeyCode("driver key code")
                .serializedParameters("[{\"name\":\"first-param\",\"type\":\"INTEGER\",\"value\":\"1\",\"id\":256},{\"name\":\"second-param\",\"type\":\"STRING\",\"value\":\"string\",\"id\":257}]")
                .build();
        assertEquals(expected, actual);
    }

    private static Parameter createParameter(final Long id, final String name, final Type type, final String value) {
        return Parameter.builder()
                .id(id)
                .name(name)
                .type(type)
                .value(value)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static LinkedHashMap<String, Parameter> createParametersByNames(final Parameter first,
                                                                            final Parameter second) {
        return Stream.of(first, second)
                .collect(
                        toMap(
                                Parameter::getName,
                                identity(),
                                (existing, replacement) -> replacement,
                                LinkedHashMap::new
                        )
                );
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static final class TestTransportableData extends TransportableData {

        @Builder
        public TestTransportableData(final long epochSeconds,
                                     final double latitude,
                                     final double longitude,
                                     final int course,
                                     final double speed,
                                     final int altitude,
                                     final int amountOfSatellites,
                                     final double hdop,
                                     final int inputs,
                                     final int outputs,
                                     final String serializedAnalogInputs,
                                     final String driverKeyCode,
                                     final String serializedParameters,
                                     final Long trackerId) {
            super(
                    epochSeconds,
                    latitude,
                    longitude,
                    course,
                    speed,
                    altitude,
                    amountOfSatellites,
                    hdop,
                    inputs,
                    outputs,
                    serializedAnalogInputs,
                    driverKeyCode,
                    serializedParameters,
                    trackerId
            );
        }
    }

    @Getter
    private static final class TestParameterView extends ParameterView {
        private final Long id;

        @Builder
        @JsonCreator
        public TestParameterView(@JsonProperty("name") final String name,
                                 @JsonProperty("type") final Type type,
                                 @JsonProperty("value") final String value,
                                 @JsonProperty("id") final Long id) {
            super(name, type, value);
            this.id = id;
        }
    }

    private static final class TestKafkaDataProducer extends KafkaDataProducer {

        public TestKafkaDataProducer(final ObjectMapper objectMapper) {
            super(null, null, null, objectMapper);
        }

        @Override
        protected TestTransportableData createTransportable(final ProducingContext context) {
            return TestTransportableData.builder()
                    .epochSeconds(context.getEpochSeconds())
                    .latitude(context.getLatitude())
                    .longitude(context.getLongitude())
                    .course(context.getCourse())
                    .speed(context.getSpeed())
                    .altitude(context.getAltitude())
                    .amountOfSatellites(context.getAmountOfSatellites())
                    .hdop(context.getHdop())
                    .inputs(context.getInputs())
                    .outputs(context.getOutputs())
                    .serializedAnalogInputs(context.getSerializedAnalogInputs())
                    .driverKeyCode(context.getDriverKeyCode())
                    .serializedParameters(context.getSerializedParameters())
                    .build();
        }

        @Override
        protected ParameterView createParameterView(final Parameter parameter) {
            return TestParameterView.builder()
                    .name(parameter.getName())
                    .type(parameter.getType())
                    .value(parameter.getValue())
                    .id(parameter.getId())
                    .build();
        }
    }
}
