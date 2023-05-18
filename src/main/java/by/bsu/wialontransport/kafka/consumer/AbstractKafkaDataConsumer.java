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
import org.apache.avro.generic.GenericRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.bsu.wialontransport.kafka.transportable.TransportableData.Fields.*;
import static java.lang.Byte.parseByte;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractKafkaDataConsumer extends AbstractKafkaGenericRecordConsumer<Long, Data> {
    private final GeographicCoordinateExtractor<Latitude> latitudeExtractor;
    private final GeographicCoordinateExtractor<Longitude> longitudeExtractor;
    private final ParametersByNamesExtractor parametersByNamesExtractor;
    private final AnalogInputsExtractor analogInputsExtractor;
    private final TrackerService trackerService;

    public AbstractKafkaDataConsumer(final TrackerService trackerService) {
        this.latitudeExtractor = new LatitudeExtractor();
        this.longitudeExtractor = new LongitudeExtractor();
        this.parametersByNamesExtractor = new ParametersByNamesExtractor();
        this.analogInputsExtractor = new AnalogInputsExtractor();
        this.trackerService = trackerService;
    }

    @Override
    protected final Data mapToData(final GenericRecord genericRecord) {
        final Long id = extractId(genericRecord);
        final LocalDateTime dateTime = extractDateTime(genericRecord);
        final Latitude latitude = this.latitudeExtractor.extract(genericRecord);
        final Longitude longitude = this.longitudeExtractor.extract(genericRecord);
        final int speed = extractSpeed(genericRecord);
        final int course = extractCourse(genericRecord);
        final int altitude = extractAltitude(genericRecord);
        final int amountOfSatellites = extractAmountOfSatellites(genericRecord);
        final double reductionPrecision = extractReductionPrecision(genericRecord);
        final int inputs = extractInputs(genericRecord);
        final int outputs = extractOutputs(genericRecord);
        final double[] analogInputs = this.analogInputsExtractor.extract(genericRecord);
        final String driverKeyCode = extractDriverKeyCode(genericRecord);
        final Map<String, Parameter> parametersByNames = this.parametersByNamesExtractor.extract(genericRecord);
        final Tracker tracker = this.extractTracker(genericRecord);
        return this.createData(
                id,
                dateTime.toLocalDate(),
                dateTime.toLocalTime(),
                latitude,
                longitude,
                speed,
                course,
                altitude,
                amountOfSatellites,
                reductionPrecision,
                inputs,
                outputs,
                analogInputs,
                driverKeyCode,
                parametersByNames,
                tracker
        );
    }

    protected abstract Data createData(final Long id,
                                       final LocalDate date,
                                       final LocalTime time,
                                       final Latitude latitude,
                                       final Longitude longitude,
                                       final int speed,
                                       final int course,
                                       final int altitude,
                                       final int amountOfSatellites,
                                       final double reductionPrecision,
                                       final int inputs,
                                       final int outputs,
                                       final double[] analogInputs,
                                       final String driverKeyCode,
                                       final Map<String, Parameter> parametersByNames,
                                       final Tracker tracker);

    private static LocalDateTime extractDateTime(final GenericRecord genericRecord) {
        return extractDateTime(genericRecord, epochSeconds);
    }

    private static Long extractId(final GenericRecord genericRecord) {
        return extractValue(genericRecord, id);
    }

    private static int extractSpeed(final GenericRecord genericRecord) {
        return extractValue(genericRecord, speed);
    }

    private static int extractCourse(final GenericRecord genericRecord) {
        return extractValue(genericRecord, course);
    }

    private static int extractAltitude(final GenericRecord genericRecord) {
        return extractValue(genericRecord, altitude);
    }

    private static int extractAmountOfSatellites(final GenericRecord genericRecord) {
        return extractValue(genericRecord, amountOfSatellites);
    }

    private static double extractReductionPrecision(final GenericRecord genericRecord) {
        return extractValue(genericRecord, reductionPrecision);
    }

    private static int extractInputs(final GenericRecord genericRecord) {
        return extractValue(genericRecord, inputs);
    }

    private static int extractOutputs(final GenericRecord genericRecord) {
        return extractValue(genericRecord, outputs);
    }

    private static String extractDriverKeyCode(final GenericRecord genericRecord) {
        return extractString(genericRecord, driverKeyCode);
    }

    private Tracker extractTracker(final GenericRecord genericRecord) {
        final Long extractedTrackerId = extractTrackerId(genericRecord);
        final Optional<Tracker> optionalTracker = this.trackerService.findById(extractedTrackerId);
        return optionalTracker.orElseThrow(
                () -> new DataConsumingException(
                        format("Tracker with id '%d' doesn't exist.", extractedTrackerId)
                )
        );
    }

    private static Long extractTrackerId(final GenericRecord genericRecord) {
        return extractValue(genericRecord, trackerId);
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
            final int degrees = this.extractDegrees(genericRecord);
            final int minutes = this.extractMinutes(genericRecord);
            final int minuteShare = this.extractMinuteShare(genericRecord);
            final char typeValue = this.extractTypeValue(genericRecord);
            return this.create(degrees, minutes, minuteShare, typeValue);
        }

        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char typeValue);

        private int extractDegrees(final GenericRecord genericRecord) {
            return extractValue(genericRecord, this.degreesGenericRecordKey);
        }

        private int extractMinutes(final GenericRecord genericRecord) {
            return extractValue(genericRecord, this.minutesGenericRecordKey);
        }

        private int extractMinuteShare(final GenericRecord genericRecord) {
            return extractValue(genericRecord, this.minuteShareGenericRecordKey);
        }

        private char extractTypeValue(final GenericRecord genericRecord) {
            return extractChar(genericRecord, this.typeValueGenericRecordKey);
        }
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

    private static final class AnalogInputsExtractor {
        private static final String REGEX_DELIMITER_SERIALIZED_ANALOG_INPUTS = ",";
        private static final String SERIALIZED_EMPTY_ANALOG_INPUTS = "";
        private static final double[] DESERIALIZED_EMPTY_ANALOG_INPUTS = new double[0];

        public double[] extract(final GenericRecord genericRecord) {
            final String serializedAnalogInputs = extractSerializedAnalogInputs(genericRecord);
            return !serializedAnalogInputs.equals(SERIALIZED_EMPTY_ANALOG_INPUTS)
                    ? deserializeNotEmptyAnalogInputs(serializedAnalogInputs)
                    : DESERIALIZED_EMPTY_ANALOG_INPUTS;
        }

        private static double[] deserializeNotEmptyAnalogInputs(final String serializedAnalogInputs) {
            return stream(serializedAnalogInputs.split(REGEX_DELIMITER_SERIALIZED_ANALOG_INPUTS))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
        }

        private static String extractSerializedAnalogInputs(final GenericRecord genericRecord) {
            return extractString(genericRecord, serializedAnalogInputs);
        }
    }

    private static final class ParametersByNamesExtractor {
        private static final String REGEX_DELIMITER_SERIALIZED_PARAMETERS = ",";

        private static final String REGEX_SERIALIZED_PARAMETER = "^(([a-zA-Z0-9]+):([123]):(.+))$";
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
            final String serializedParametersString = extractSerializedParameters(genericRecord);
            return serializedParametersString.split(REGEX_DELIMITER_SERIALIZED_PARAMETERS);
        }

        private static String extractSerializedParameters(final GenericRecord genericRecord) {
            return extractString(genericRecord, serializedParameters);
        }

        private static Map<String, Parameter> deserializeToParametersByNames(final String[] serializedParameters) {
            return stream(serializedParameters)
                    .map(ParametersByNamesExtractor::deserializeParameter)
                    .collect(toMap(Parameter::getName, identity()));
        }

        private static Parameter deserializeParameter(final String serializedParameter) {
            final Matcher matcher = findMatcherOrThrowExceptionIfNotMatch(serializedParameter);
            return createParameter(
                    extractName(matcher),
                    extractType(matcher),
                    extractValue(matcher)
            );
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
