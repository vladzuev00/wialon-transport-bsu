package by.bsu.wialontransport.kafka.producer.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.GenericRecordKafkaProducer;
import by.bsu.wialontransport.kafka.model.view.ParameterView;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.ZoneOffset;
import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.collectValuesToList;
import static java.time.ZoneOffset.UTC;
import static lombok.AccessLevel.NONE;

public abstract class KafkaDataProducer extends GenericRecordKafkaProducer<Long, TransportableData, Data> {
    private static final ZoneOffset ZONE_OFFSET = UTC;

    private final ObjectMapper objectMapper;

    public KafkaDataProducer(final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                             final String topicName,
                             final Schema schema,
                             final ObjectMapper objectMapper) {
        super(kafkaTemplate, topicName, schema);
        this.objectMapper = objectMapper;
    }

    @Override
    protected final TransportableData mapToTransportable(final Data data) {
        final CreatingTransportableContext context = createCreatingContext(data);
        return createTransportable(context);
    }

    protected abstract TransportableData createTransportable(final CreatingTransportableContext context);

    protected abstract ParameterView createParameterView(final Parameter parameter);

    private CreatingTransportableContext createCreatingContext(final Data data) {
        return CreatingTransportableContext.builder()
                .data(data)
                .epochSeconds(findEpochSeconds(data))
                .serializedAnalogInputs(serializeAnalogInputs(data))
                .serializedParameters(serializeParameters(data))
                .build();
    }

    private static long findEpochSeconds(final Data data) {
        return data.getDateTime().toEpochSecond(ZONE_OFFSET);
    }

    private String serializeAnalogInputs(final Data data) {
        return serializeToJson(data.getAnalogInputs());
    }

    private String serializeParameters(final Data data) {
        final List<ParameterView> parameterViews = createParameterViews(data);
        return serializeToJson(parameterViews);
    }

    private List<ParameterView> createParameterViews(final Data data) {
        return collectValuesToList(data.getParametersByNames(), this::createParameterView);
    }

    private String serializeToJson(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException cause) {
            throw new SerializationException(cause);
        }
    }

    @RequiredArgsConstructor
    @Builder
    protected static final class CreatingTransportableContext {

        @Getter(NONE)
        private final Data data;

        @Getter
        private final long epochSeconds;

        @Getter
        private final String serializedAnalogInputs;

        @Getter
        private final String serializedParameters;

        public double getLatitude() {
            return data.getCoordinate().getLatitude();
        }

        public double getLongitude() {
            return data.getCoordinate().getLongitude();
        }

        public int getCourse() {
            return data.getCourse();
        }

        public double getSpeed() {
            return data.getSpeed();
        }

        public int getAltitude() {
            return data.getAltitude();
        }

        public int getAmountOfSatellites() {
            return data.getAmountOfSatellites();
        }

        public double getReductionPrecision() {
            return data.getReductionPrecision();
        }

        public int getInputs() {
            return data.getInputs();
        }

        public int getOutputs() {
            return data.getOutputs();
        }

        public String getDriverKeyCode() {
            return data.getDriverKeyCode();
        }

        public Long getTrackerId() {
            return data.getTracker().getId();
        }

        public Long getDataId() {
            return data.getId();
        }

        public Long getAddressId() {
            return data.getAddress().getId();
        }
    }

    static final class SerializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public SerializationException() {

        }

        @SuppressWarnings("unused")
        public SerializationException(final String description) {
            super(description);
        }

        public SerializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public SerializationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
