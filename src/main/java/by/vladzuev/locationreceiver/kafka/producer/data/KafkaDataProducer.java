package by.vladzuev.locationreceiver.kafka.producer.data;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.kafka.producer.GenericRecordKafkaProducer;
import by.vladzuev.locationreceiver.kafka.model.view.ParameterView;
import by.vladzuev.locationreceiver.kafka.model.transportable.data.TransportableData;
import by.vladzuev.locationreceiver.util.CollectionUtil;
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

import static java.time.ZoneOffset.UTC;
import static lombok.AccessLevel.NONE;

public abstract class KafkaDataProducer extends GenericRecordKafkaProducer<Long, TransportableData, Location> {
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
    protected final TransportableData mapToTransportable(final Location data) {
        final ProducingContext context = createCreatingContext(data);
        return createTransportable(context);
    }

    protected abstract TransportableData createTransportable(final ProducingContext context);

    protected abstract ParameterView createParameterView(final Parameter parameter);

    private ProducingContext createCreatingContext(final Location data) {
        return ProducingContext.builder()
                .data(data)
                .epochSeconds(findEpochSeconds(data))
                .serializedAnalogInputs(serializeAnalogInputs(data))
                .serializedParameters(serializeParameters(data))
                .build();
    }

    private static long findEpochSeconds(final Location data) {
        return data.getDateTime().toEpochSecond(ZONE_OFFSET);
    }

    private String serializeAnalogInputs(final Location data) {
        return serializeToJson(data.getAnalogInputs());
    }

    private String serializeParameters(final Location data) {
        final List<ParameterView> parameterViews = createParameterViews(data);
        return serializeToJson(parameterViews);
    }

    private List<ParameterView> createParameterViews(final Location data) {
        return CollectionUtil.collectValuesToList(data.getParametersByNames(), this::createParameterView);
    }

    private String serializeToJson(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException cause) {
            throw new ProducingException(cause);
        }
    }

    @RequiredArgsConstructor
    @Builder
    protected static final class ProducingContext {

        @Getter(NONE)
        private final Location data;

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
            return data.getSatelliteCount();
        }

        public double getHdop() {
            return data.getHdop();
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

    static final class ProducingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ProducingException() {

        }

        @SuppressWarnings("unused")
        public ProducingException(final String description) {
            super(description);
        }

        public ProducingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ProducingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
