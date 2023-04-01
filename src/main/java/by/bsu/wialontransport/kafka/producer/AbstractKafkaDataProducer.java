package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.GeographicCoordinate;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.kafka.transportable.TransportableData;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.joining;

public abstract class AbstractKafkaDataProducer extends AbstractGenericRecordKafkaProducer<Long, TransportableData, Data> {
    private static final String DELIMITER_SERIALIZED_PARAMETER_PROPERTIES = ",";
    private static final String DELIMITER_SERIALIZED_PARAMETERS = ";";

    public AbstractKafkaDataProducer(final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                     final String topicName,
                                     final Schema schema) {
        super(kafkaTemplate, topicName, schema);
    }

    @Override
    protected Long findKey(final TransportableData transportableData) {
        return transportableData.trackerId();
    }

    @Override
    protected TransportableData mapToTransportable(final Data data) {
        return new TransportableData(
                data.getId(),
                findEpochSecondsOfDateTime(data),
                findLatitudeDegrees(data),
                findLatitudeMinutes(data),
                findLatitudeMinuteShare(data),
                findLatitudeTypeValue(data),
                findLongitudeDegrees(data),
                findLongitudeMinutes(data),
                findLongitudeMinuteShare(data),
                findLongitudeTypeValue(data),
                data.getSpeed(),
                data.getCourse(),
                data.getAltitude(),
                data.getAmountOfSatellites(),
                data.getReductionPrecision(),
                data.getInputs(),
                data.getOutputs(),
                data.getAnalogInputs(),
                data.getDriverKeyCode(),
                serializeParameters(data),
                findTrackerId(data)
        );
    }

    private static long findEpochSecondsOfDateTime(final Data data) {
        final LocalDateTime dateTime = LocalDateTime.of(data.getDate(), data.getTime());
        return dateTime.toEpochSecond(UTC);
    }

    private static int findLatitudeDegrees(final Data data) {
        return findLatitudeIntFieldValue(data, Latitude::getDegrees);
    }

    private static int findLatitudeMinutes(final Data data) {
        return findLatitudeIntFieldValue(data, Latitude::getMinutes);
    }

    private static int findLatitudeMinuteShare(final Data data) {
        return findLatitudeIntFieldValue(data, Latitude::getMinuteShare);
    }

    private static char findLatitudeTypeValue(final Data data) {
        final Latitude latitude = data.getLatitude();
        final DataEntity.Latitude.Type latitudeType = latitude.getType();
        return latitudeType.getValue();
    }

    private static int findLongitudeDegrees(final Data data) {
        return findLongitudeIntFieldValue(data, Longitude::getDegrees);
    }

    private static int findLongitudeMinutes(final Data data) {
        return findLongitudeIntFieldValue(data, Longitude::getMinutes);
    }

    private static int findLongitudeMinuteShare(final Data data) {
        return findLongitudeIntFieldValue(data, Longitude::getMinuteShare);
    }

    private static char findLongitudeTypeValue(final Data data) {
        final Longitude longitude = data.getLongitude();
        final DataEntity.Longitude.Type longitudeType = longitude.getType();
        return longitudeType.getValue();
    }

    private static int findLatitudeIntFieldValue(final Data data, final ToIntFunction<Latitude> getterValue) {
        return findGeographicCoordinateIntFieldValue(data, Data::getLatitude, getterValue);
    }

    private static int findLongitudeIntFieldValue(final Data data, final ToIntFunction<Longitude> getterValue) {
        return findGeographicCoordinateIntFieldValue(data, Data::getLongitude, getterValue);
    }

    private static <T extends GeographicCoordinate> int findGeographicCoordinateIntFieldValue(
            final Data data, final Function<Data, T> getterGeographicCoordinate, final ToIntFunction<T> getterValue) {
        final T geographicCoordinate = getterGeographicCoordinate.apply(data);
        return getterValue.applyAsInt(geographicCoordinate);
    }

    private static String serializeParameters(final Data data) {
        final Collection<Parameter> parameters = findParameters(data);
        return serializeParameters(parameters);
    }

    private static Collection<Parameter> findParameters(final Data data) {
        final Map<String, Parameter> parametersByNames = data.getParametersByNames();
        return parametersByNames.values();
    }

    private static String serializeParameters(final Collection<Parameter> parameters) {
        return parameters.stream()
                .map(AbstractKafkaDataProducer::serializeParameter)
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

    private static Long findTrackerId(final Data data) {
        final Tracker tracker = data.getTracker();
        return tracker.getId();
    }
}
