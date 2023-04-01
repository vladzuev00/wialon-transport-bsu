package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.GeographicCoordinate;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import by.bsu.wialontransport.kafka.transportable.TransportableData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.bsu.wialontransport.kafka.transportable.TransportableData.Fields.*;
import static java.lang.Byte.parseByte;
import static java.lang.String.format;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;

@Component
public final class KafkaInboundDataConsumer extends AbstractKafkaGenericRecordConsumer<Long, Data> {
    private static final String EXCEPTION_MESSAGE_NO_TRACKER_WITH_GIVEN_ID = "Tracker with id '%d' doesn't exist.";

    private final GeographicCoordinateExtractor<Latitude> latitudeExtractor;
    private final GeographicCoordinateExtractor<Longitude> longitudeExtractor;
    private final ParametersByNamesExtractor parametersByNamesExtractor;
    private final TrackerService trackerService;

    public KafkaInboundDataConsumer(final TrackerService trackerService) {
        this.latitudeExtractor = new LatitudeExtractor();
        this.longitudeExtractor = new LongitudeExtractor();
        this.parametersByNamesExtractor = new ParametersByNamesExtractor();
        this.trackerService = trackerService;
    }

    @Override
    protected Data mapToData(final GenericRecord genericRecord) {
        final LocalDateTime dateTime = extractDateTime(genericRecord);
        return new Data(
                extractValue(genericRecord, id),
                dateTime.toLocalDate(),
                dateTime.toLocalTime(),
                this.latitudeExtractor.extract(genericRecord),
                this.longitudeExtractor.extract(genericRecord),
                extractValue(genericRecord, speed),
                extractValue(genericRecord, course),
                extractValue(genericRecord, altitude),
                extractValue(genericRecord, amountOfSatellites),
                extractValue(genericRecord, reductionPrecision),
                extractValue(genericRecord, inputs),
                extractValue(genericRecord, outputs),
                extractValue(genericRecord, analogInputs),
                extractValue(genericRecord, driverKeyCode),
                this.parametersByNamesExtractor.extract(genericRecord),
                this.extractTracker(genericRecord)
        );
    }

    @Override
    protected void processData(final Data data) {

    }

    @SuppressWarnings("unchecked")
    private static <T> T extractValue(final GenericRecord genericRecord, final String genericRecordKey) {
        return (T) genericRecord.get(genericRecordKey);
    }

    private static LocalDateTime extractDateTime(final GenericRecord genericRecord) {
        final long epochSeconds = extractValue(genericRecord, TransportableData.Fields.epochSeconds);
        return ofEpochSecond(epochSeconds, 0, UTC);
    }

    private Tracker extractTracker(final GenericRecord genericRecord) {
        final Long extractedTrackerId = extractValue(genericRecord, trackerId);
        final Optional<Tracker> optionalTracker = this.trackerService.findById(extractedTrackerId);
        return optionalTracker.orElseThrow(
                () -> new DataConsumingException(
                        format(EXCEPTION_MESSAGE_NO_TRACKER_WITH_GIVEN_ID, extractedTrackerId)
                )
        );
    }

    private static abstract class GeographicCoordinateExtractor<T extends GeographicCoordinate> {
        private final String degreesGenericRecordKey;
        private final String minutesGenericRecordKey;
        private final String minuteShareGenericRecordKey;
        private final String typeValueGenericRecordKey;

        public GeographicCoordinateExtractor(final String degreesGenericRecordKey,
                                             final String minutesGenericRecordKey,
                                             final String minuteShareGenericRecordKey,
                                             final String typeValueGenericRecordKey) {
            this.degreesGenericRecordKey = degreesGenericRecordKey;
            this.minutesGenericRecordKey = minutesGenericRecordKey;
            this.minuteShareGenericRecordKey = minuteShareGenericRecordKey;
            this.typeValueGenericRecordKey = typeValueGenericRecordKey;
        }

        public final T extract(final GenericRecord genericRecord) {
            final int degrees = extractValue(genericRecord, this.degreesGenericRecordKey);
            final int minutes = extractValue(genericRecord, this.minutesGenericRecordKey);
            final int minuteShare = extractValue(genericRecord, this.minuteShareGenericRecordKey);
            final char typeValue = extractValue(genericRecord, this.typeValueGenericRecordKey);
            return this.create(degrees, minutes, minuteShare, typeValue);
        }

        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char typeValue);
    }

    private static final class LatitudeExtractor extends GeographicCoordinateExtractor<Latitude> {

        public LatitudeExtractor() {
            super(latitudeDegrees, latitudeMinutes, latitudeMinuteShare, latitudeTypeValue);
        }

        @Override
        protected Latitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final DataEntity.Latitude.Type type = DataEntity.Latitude.Type.findByValue(typeValue);
            return new Latitude(degrees, minutes, minuteShare, type);
        }
    }

    private static final class LongitudeExtractor extends GeographicCoordinateExtractor<Longitude> {

        public LongitudeExtractor() {
            super(longitudeDegrees, longitudeMinutes, longitudeMinuteShare, longitudeTypeValue);
        }

        @Override
        protected Longitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final DataEntity.Longitude.Type type = DataEntity.Longitude.Type.findByValue(typeValue);
            return new Longitude(degrees, minutes, minuteShare, type);
        }
    }

    private static final class ParametersByNamesExtractor {
        private static final String REGEX_DELIMITER_SERIALIZED_PARAMETERS = ";";

        private static final String REGEX_SERIALIZED_PARAMETER = "^(([a-zA-Z0-9]+),([123]),(.+))$";
        private static final Pattern PATTERN_SERIALIZED_PARAMETER = compile(REGEX_SERIALIZED_PARAMETER);
        private static final int GROUP_NUMBER_NAME = 2;
        private static final int GROUP_NUMBER_TYPE_VALUE = 3;
        private static final int GROUP_NUMBER_VALUE = 4;

        private static final String MESSAGE_EXCEPTION_NOT_VALID_SERIALIZED_PARAMETER
                = "Given serialized parameter isn't valid: %s";

        public Map<String, Parameter> extract(final GenericRecord genericRecord) {
            final String[] serializedParameters = findSerializedParameters(genericRecord);
            return deserializeToParametersByNames(serializedParameters);
        }

        private static String[] findSerializedParameters(final GenericRecord genericRecord) {
            final String serializedParametersString = KafkaInboundDataConsumer.extractValue(
                    genericRecord, serializedParameters);
            return serializedParametersString.split(REGEX_DELIMITER_SERIALIZED_PARAMETERS);
        }

        private static Map<String, Parameter> deserializeToParametersByNames(final String[] serializedParameters) {
            return stream(serializedParameters)
                    .map(ParametersByNamesExtractor::deserializeParameter)
                    .collect(toMap(Parameter::getName, identity()));
        }

        private static Parameter deserializeParameter(final String serializedParameter) {
            final Matcher matcher = findMatcherOrThrowExceptionIfNotMatch(serializedParameter);
            final String name = extractName(matcher);
            final ParameterEntity.Type type = extractType(matcher);
            final String value = extractValue(matcher);
            return createParameter(name, type, value);
        }

        private static Matcher findMatcherOrThrowExceptionIfNotMatch(final String serializedParameter) {
            final Matcher matcher = PATTERN_SERIALIZED_PARAMETER.matcher(serializedParameter);
            if (!matcher.matches()) {
                throw new DataConsumingException(
                        format(MESSAGE_EXCEPTION_NOT_VALID_SERIALIZED_PARAMETER, serializedParameter)
                );
            }
            return matcher;
        }

        private static String extractName(final Matcher matcher) {
            return matcher.group(GROUP_NUMBER_NAME);
        }

        private static ParameterEntity.Type extractType(final Matcher matcher) {
            final String typeValueString = matcher.group(GROUP_NUMBER_TYPE_VALUE);
            final byte typeValue = parseByte(typeValueString);
            return ParameterEntity.Type.findByValue(typeValue);
        }

        private static String extractValue(final Matcher matcher) {
            return matcher.group(GROUP_NUMBER_VALUE);
        }

        private static Parameter createParameter(final String name, final ParameterEntity.Type type,
                                                 final String value) {
            return Parameter.builder()
                    .name(name)
                    .type(type)
                    .value(value)
                    .build();
        }
    }
}
