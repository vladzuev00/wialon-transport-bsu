package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.kafka.transportable.TransportableData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Map;

import static java.time.ZoneOffset.UTC;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public abstract class AbstractKafkaDataProducer<T extends TransportableData>
        extends AbstractGenericRecordKafkaProducer<Long, T, Data> {
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
    protected final T mapToTransportable(final Data data) {

    }

    protected abstract T createTransportable(final CreatingTransportableContext context);

    //TODO: do hierarhy for views: one super class, one class without id + one class with id
    protected abstract Object createParameterView(final Parameter parameter);

    private CreatingTransportableContext createCreatingContext(final Data data) {

    }

    private static long findEpochSeconds(final Data data) {
        return data.getDateTime().toEpochSecond(ZONE_OFFSET);
    }

    private String serializeParameters(final Data data) {

    }

    protected static int findLatitudeDegrees(final Data data) {
//        return findLatitudeIntFieldValue(data, Latitude::getDegrees);
        return 0;
    }

    protected static int findLatitudeMinutes(final Data data) {
//        return findLatitudeIntFieldValue(data, Latitude::getMinutes);
        return 0;
    }

    protected static int findLatitudeMinuteShare(final Data data) {
//        return findLatitudeIntFieldValue(data, Latitude::getMinuteShare);
        return 0;
    }

    protected static char findLatitudeTypeValue(final Data data) {
//        final Latitude latitude = data.getLatitude();
//        final DataEntity.Latitude.Type latitudeType = latitude.getType();
//        return latitudeType.getValue();
        return 'a';
    }

    protected static int findLongitudeDegrees(final Data data) {
//        return findLongitudeIntFieldValue(data, Longitude::getDegrees);
        return 0;
    }

    protected static int findLongitudeMinutes(final Data data) {
//        return findLongitudeIntFieldValue(data, Longitude::getMinutes);
        return 0;
    }

    protected static int findLongitudeMinuteShare(final Data data) {
//        return findLongitudeIntFieldValue(data, Longitude::getMinuteShare);
        return 0;
    }

    protected static char findLongitudeTypeValue(final Data data) {
//        final Longitude longitude = data.getLongitude();
//        final DataEntity.Longitude.Type longitudeType = longitude.getType();
//        return longitudeType.getValue();
        return 'a';
    }

    protected final String serializeAnalogInputs(final Data data) {
        return this.analogInputsSerializer.serialize(data);
    }

    protected final String serializeParameters(final Data data) {
        return this.parametersSerializer.serialize(data);
    }

    protected static Long findTrackerId(final Data data) {
        final Tracker tracker = data.getTracker();
        return tracker.getId();
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

    private static final class AnalogInputsSerializer {
        private static final String DELIMITER_SERIALIZED_ANALOG_INPUTS = ",";

        public String serialize(final Data data) {
            return stream(data.getAnalogInputs())
                    .mapToObj(Double::toString)
                    .collect(joining(DELIMITER_SERIALIZED_ANALOG_INPUTS));
        }
    }

    private static final class ParametersSerializer {
        private static final String DELIMITER_SERIALIZED_PARAMETER_PROPERTIES = ":";
        private static final String DELIMITER_SERIALIZED_PARAMETERS = ",";

        public String serialize(final Data data) {
            final Collection<Parameter> parameters = findParameters(data);
            return serializeParameters(parameters);
        }

        private static Collection<Parameter> findParameters(final Data data) {
            final Map<String, Parameter> parametersByNames = data.getParametersByNames();
            return parametersByNames.values();
        }

        private static String serializeParameters(final Collection<Parameter> parameters) {
            return parameters.stream()
                    .map(ParametersSerializer::serializeParameter)
                    .collect(joining(DELIMITER_SERIALIZED_PARAMETERS));
        }

        private static String serializeParameter(final Parameter parameter) {
            final StringBuilder builderSerializedParameter = new StringBuilder();
            appendParameterIdWithPropertyDelimiterIfIdExists(builderSerializedParameter, parameter);
            builderSerializedParameter.append(parameter.getName());
            builderSerializedParameter.append(DELIMITER_SERIALIZED_PARAMETER_PROPERTIES);
            appendValueOfParameterType(builderSerializedParameter, parameter);
            builderSerializedParameter.append(DELIMITER_SERIALIZED_PARAMETER_PROPERTIES);
            builderSerializedParameter.append(parameter.getValue());
            return builderSerializedParameter.toString();
        }

        private static void appendParameterIdWithPropertyDelimiterIfIdExists(final StringBuilder stringBuilder,
                                                                             final Parameter parameter) {
            if (parameter.getId() != null) {
                stringBuilder.append(parameter.getId());
                stringBuilder.append(DELIMITER_SERIALIZED_PARAMETER_PROPERTIES);
            }
        }

        private static void appendValueOfParameterType(final StringBuilder stringBuilder, final Parameter parameter) {
            final ParameterEntity.Type type = parameter.getType();
            stringBuilder.append(type.getValue());
        }
    }
}
