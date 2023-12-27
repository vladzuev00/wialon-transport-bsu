package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.view.ParameterView;
import by.bsu.wialontransport.kafka.transportable.data.TransportableData;
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

public abstract class AbstractKafkaDataProducer extends AbstractGenericRecordKafkaProducer<Long, TransportableData, Data> {
    private static final ZoneOffset ZONE_OFFSET = UTC;

    private final ObjectMapper objectMapper;

    public AbstractKafkaDataProducer(final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
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
    @Getter
    @Builder
    protected static final class CreatingTransportableContext {
        private final Data data;
        private final long epochSeconds;
        private final String serializedAnalogInputs;
        private final String serializedParameters;
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
