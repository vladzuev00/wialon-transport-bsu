package by.bsu.wialontransport.protocol.wialon.decoder.location.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.location.parser.exception.NotValidSubMessageException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toUnmodifiableSet;

public final class WialonLocationComponentParser {
    private static final String LOCATION_REGEX
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"                     //date, time
            + "(((\\d{2})(\\d{2})\\.(\\d+);([NS]))|(NA;NA));"      //latitude
            + "(((\\d{3})(\\d{2})\\.(\\d+);([EW]))|(NA;NA));"      //longitude
            + "(\\d+|(NA));"                                       //speed
            + "(\\d+|(NA));"                                       //course
            + "(\\d+|(NA));"                                       //altitude
            + "(\\d+|(NA));"                                       //satelliteCount
            + "((\\d+\\.\\d+)|(NA));"                              //hdop
            + "(\\d+|(NA));"                                       //inputs
            + "(\\d+|(NA));"                                       //outputs
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"                      //analogInputs
            + "(.*);"                                              //driverKeyCode
            + "((([^:]+:[123]:[^,:]+)(,([^:]+:[123]:[^,:]+))*)|)"; //parameters
    private static final Pattern LOCATION_PATTERN = compile(LOCATION_REGEX);
    private static final String NA = "NA";

    private final Matcher matcher;
    private final DateParser dateParser;
    private final TimeParser timeParser;
    private final LatitudeParser latitudeParser;
    private final LongitudeParser longitudeParser;
    private final SpeedParser speedParser;
    private final CourseParser courseParser;
    private final AltitudeParser altitudeParser;
    private final SatelliteCountParser satelliteCountParser;
    private final HdopParser hdopParser;
    private final InputsParser inputsParser;
    private final OutputsParser outputsParser;
    private final AnalogInputsParser analogInputsParser;
    private final DriverKeyCodeParser driverKeyCodeParser;
    private final ParametersParser parametersParser;

    public WialonLocationComponentParser(final String source) {
        matcher = LOCATION_PATTERN.matcher(source);
        match(source);
        dateParser = new DateParser();
        timeParser = new TimeParser();
        latitudeParser = new LatitudeParser();
        longitudeParser = new LongitudeParser();
        speedParser = new SpeedParser();
        courseParser = new CourseParser();
        altitudeParser = new AltitudeParser();
        satelliteCountParser = new SatelliteCountParser();
        hdopParser = new HdopParser();
        inputsParser = new InputsParser();
        outputsParser = new OutputsParser();
        analogInputsParser = new AnalogInputsParser();
        driverKeyCodeParser = new DriverKeyCodeParser();
        parametersParser = new ParametersParser();
    }

    public Optional<LocalDate> parseDate() {
        return dateParser.parse();
    }

    public Optional<LocalTime> parseTime() {
        return timeParser.parse();
    }

    public OptionalDouble parseLatitude() {
        return latitudeParser.parse();
    }

    public OptionalDouble parseLongitude() {
        return longitudeParser.parse();
    }

    public OptionalInt parseCourse() {
        return courseParser.parse();
    }

    public OptionalDouble parseSpeed() {
        return speedParser.parse();
    }

    public OptionalInt parseAltitude() {
        return altitudeParser.parse();
    }

    public OptionalInt parseSatelliteCount() {
        return satelliteCountParser.parse();
    }

    public OptionalDouble parseHdop() {
        return hdopParser.parse();
    }

    public OptionalInt parseInputs() {
        return inputsParser.parse();
    }

    public OptionalInt parseOutputs() {
        return outputsParser.parse();
    }

    public Optional<double[]> parseAnalogInputs() {
        return analogInputsParser.parse();
    }

    public Optional<String> parseDriverKeyCode() {
        return driverKeyCodeParser.parse();
    }

    public Optional<Set<Parameter>> parseParameters() {
        return parametersParser.parse();
    }

    private void match(final String source) {
        if (!matcher.matches()) {
            throw new NotValidSubMessageException("Given source isn't valid: '%s'".formatted(source));
        }
    }

    @RequiredArgsConstructor
    private abstract class ComponentParser<T> {
        private final int groupNumber;
        private final String notDefinedContent;

        public final T parse() {
            final String content = matcher.group(groupNumber);
            return !Objects.equals(content, notDefinedContent) ? parseDefined(content) : createNotDefinedComponent();
        }

        protected abstract T parseDefined(final String content);

        protected abstract T createNotDefinedComponent();
    }

    private final class DateParser extends ComponentParser<Optional<LocalDate>> {
        private static final int GROUP_NUMBER = 2;
        private static final String FORMAT = "ddMMyy";
        private static final DateTimeFormatter FORMATTER = ofPattern(FORMAT);

        public DateParser() {
            super(GROUP_NUMBER, NA);
        }

        @Override
        protected Optional<LocalDate> parseDefined(final String content) {
            return Optional.of(LocalDate.parse(content, FORMATTER));
        }

        @Override
        protected Optional<LocalDate> createNotDefinedComponent() {
            return Optional.empty();
        }
    }

    private final class TimeParser extends ComponentParser<Optional<LocalTime>> {
        private static final int GROUP_NUMBER = 4;
        private static final String FORMAT = "HHmmss";
        private static final DateTimeFormatter FORMATTER = ofPattern(FORMAT);

        public TimeParser() {
            super(GROUP_NUMBER, NA);
        }

        @Override
        protected Optional<LocalTime> parseDefined(final String content) {
            return Optional.of(LocalTime.parse(content, FORMATTER));
        }

        @Override
        protected Optional<LocalTime> createNotDefinedComponent() {
            return Optional.empty();
        }
    }

    private abstract class CoordinateParser extends ComponentParser<OptionalDouble> {
        private static final String NOT_DEFINED_CONTENT = NA + ";" + NA;

        private final int groupNumberDegrees;
        private final int groupNumberMinutes;
        private final int groupNumberMinuteShare;
        private final int groupNumberHemisphere;
        private final char hemisphereReplacingSign;

        public CoordinateParser(final int groupNumber,
                                final int groupNumberDegrees,
                                final int groupNumberMinutes,
                                final int groupNumberMinuteShare,
                                final int groupNumberHemisphere,
                                final char hemisphereReplacingSign) {
            super(groupNumber, NOT_DEFINED_CONTENT);
            this.groupNumberDegrees = groupNumberDegrees;
            this.groupNumberMinutes = groupNumberMinutes;
            this.groupNumberMinuteShare = groupNumberMinuteShare;
            this.groupNumberHemisphere = groupNumberHemisphere;
            this.hemisphereReplacingSign = hemisphereReplacingSign;
        }

        @Override
        protected final OptionalDouble parseDefined(final String content) {
            final int degrees = extractIntGroup(groupNumberDegrees);
            final int minutes = extractIntGroup(groupNumberMinutes);
            final int minuteShare = extractIntGroup(groupNumberMinuteShare);
            final char hemisphere = extractCharGroup(groupNumberHemisphere);
            final double abs = degrees + (minutes / 60.) + (minuteShare / 3600.);
            final double value = Objects.equals(hemisphere, hemisphereReplacingSign) ? -1 * abs : abs;
            return OptionalDouble.of(value);
        }

        @Override
        protected final OptionalDouble createNotDefinedComponent() {
            return OptionalDouble.empty();
        }

        private int extractIntGroup(final int groupNumber) {
            return parseInt(matcher.group(groupNumber));
        }

        private char extractCharGroup(final int groupNumber) {
            return matcher.group(groupNumber).charAt(0);
        }
    }

    private final class LatitudeParser extends CoordinateParser {
        private static final int GROUP_NUMBER = 6;
        private static final int GROUP_NUMBER_DEGREES = 8;
        private static final int GROUP_NUMBER_MINUTES = 9;
        private static final int GROUP_NUMBER_MINUTE_SHARE = 10;
        private static final int GROUP_NUMBER_HEMISPHERE = 11;
        private static final char HEMISPHERE_REPLACING_SIGN = 'N';

        public LatitudeParser() {
            super(
                    GROUP_NUMBER,
                    GROUP_NUMBER_DEGREES,
                    GROUP_NUMBER_MINUTES,
                    GROUP_NUMBER_MINUTE_SHARE,
                    GROUP_NUMBER_HEMISPHERE,
                    HEMISPHERE_REPLACING_SIGN
            );
        }
    }

    private final class LongitudeParser extends CoordinateParser {
        private static final int GROUP_NUMBER = 13;
        private static final int GROUP_NUMBER_DEGREES = 15;
        private static final int GROUP_NUMBER_MINUTES = 16;
        private static final int GROUP_NUMBER_MINUTE_SHARE = 17;
        private static final int GROUP_NUMBER_HEMISPHERE = 18;
        private static final char HEMISPHERE_REPLACING_SIGN = 'W';

        public LongitudeParser() {
            super(
                    GROUP_NUMBER,
                    GROUP_NUMBER_DEGREES,
                    GROUP_NUMBER_MINUTES,
                    GROUP_NUMBER_MINUTE_SHARE,
                    GROUP_NUMBER_HEMISPHERE,
                    HEMISPHERE_REPLACING_SIGN
            );
        }
    }

    private abstract class IntComponentParser extends ComponentParser<OptionalInt> {

        public IntComponentParser(final int groupNumber) {
            super(groupNumber, NA);
        }

        @Override
        protected final OptionalInt parseDefined(final String content) {
            return OptionalInt.of(parseInt(content));
        }

        @Override
        protected final OptionalInt createNotDefinedComponent() {
            return OptionalInt.empty();
        }
    }

    private abstract class DoubleComponentParser extends ComponentParser<OptionalDouble> {

        public DoubleComponentParser(final int groupNumber) {
            super(groupNumber, NA);
        }

        @Override
        protected final OptionalDouble parseDefined(final String content) {
            return OptionalDouble.of(parseDouble(content));
        }

        @Override
        protected final OptionalDouble createNotDefinedComponent() {
            return OptionalDouble.empty();
        }
    }

    private final class SpeedParser extends DoubleComponentParser {
        private static final int GROUP_NUMBER = 20;

        public SpeedParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class CourseParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 22;

        public CourseParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class AltitudeParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 24;

        public AltitudeParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class SatelliteCountParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 26;

        public SatelliteCountParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class HdopParser extends DoubleComponentParser {
        private static final int GROUP_NUMBER = 28;

        public HdopParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class InputsParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 31;

        public InputsParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class OutputsParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 33;

        public OutputsParser() {
            super(GROUP_NUMBER);
        }
    }

    private final class AnalogInputsParser extends ComponentParser<Optional<double[]>> {
        private static final int GROUP_NUMBER = 35;
        private static final String DELIMITER = ",";

        public AnalogInputsParser() {
            super(GROUP_NUMBER, NA);
        }

        @Override
        protected Optional<double[]> parseDefined(final String content) {
            final double[] analogInputs = stream(content.split(DELIMITER))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            return Optional.of(analogInputs);
        }

        @Override
        protected Optional<double[]> createNotDefinedComponent() {
            return Optional.empty();
        }
    }

    private final class DriverKeyCodeParser extends ComponentParser<Optional<String>> {
        private static final int GROUP_NUMBER = 40;

        public DriverKeyCodeParser() {
            super(GROUP_NUMBER, NA);
        }

        @Override
        protected Optional<String> parseDefined(final String content) {
            return Optional.of(content);
        }

        @Override
        protected Optional<String> createNotDefinedComponent() {
            return Optional.empty();
        }
    }

    private final class ParametersParser extends ComponentParser<Optional<Set<Parameter>>> {
        private static final int GROUP_NUMBER = 41;
        private static final String EMPTY_STRING = "";
        private static final String DELIMITER_PARAMETERS = ",";

        private final ParameterParser parameterParser;

        public ParametersParser() {
            super(GROUP_NUMBER, EMPTY_STRING);
            parameterParser = new ParameterParser();
        }

        @Override
        protected Optional<Set<Parameter>> parseDefined(final String content) {
            final Set<Parameter> parameters = stream(content.split(DELIMITER_PARAMETERS))
                    .map(parameterParser::parse)
                    .collect(toUnmodifiableSet());
            return Optional.of(parameters);
        }

        @Override
        protected Optional<Set<Parameter>> createNotDefinedComponent() {
            return Optional.empty();
        }
    }

    private static final class ParameterParser {
        private static final String INTEGER_ALIAS = "1";
        private static final String DOUBLE_ALIAS = "2";
        private static final String STRING_ALIAS = "3";
        private static final Map<String, ParameterEntity.Type> TYPES_BY_ALIASES = Map.of(
                INTEGER_ALIAS, INTEGER,
                DOUBLE_ALIAS, DOUBLE,
                STRING_ALIAS, STRING
        );
        private static final String COMPONENTS_DELIMITER = ":";
        private static final int NAME_INDEX = 0;
        private static final int TYPE_INDEX = 1;
        private static final int VALUE_INDEX = 2;

        public Parameter parse(final String content) {
            final String[] components = content.split(COMPONENTS_DELIMITER);
            final String name = components[NAME_INDEX];
            final ParameterEntity.Type type = TYPES_BY_ALIASES.get(components[TYPE_INDEX]);
            final String value = components[VALUE_INDEX];
            return Parameter.builder()
                    .name(name)
                    .type(type)
                    .value(value)
                    .build();
        }
    }
}
