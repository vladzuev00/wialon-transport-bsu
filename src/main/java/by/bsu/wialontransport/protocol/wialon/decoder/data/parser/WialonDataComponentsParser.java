package by.bsu.wialontransport.protocol.wialon.decoder.data.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.data.parser.exception.NotValidSubMessageException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toUnmodifiableSet;

public final class WialonDataComponentsParser {
    private static final String DATA_REGEX
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"                     //date, time
            + "(((\\d{2})(\\d{2})\\.(\\d+);([NS]))|(NA;NA));"      //latitude
            + "(((\\d{3})(\\d{2})\\.(\\d+);([EW]))|(NA;NA));"      //longitude
            + "(\\d+|(NA));"                                       //speed
            + "(\\d+|(NA));"                                       //course
            + "(\\d+|(NA));"                                       //altitude
            + "(\\d+|(NA));"                                       //amountOfSatellites
            + "((\\d+\\.\\d+)|(NA));"                              //hdop
            + "(\\d+|(NA));"                                       //inputs
            + "(\\d+|(NA));"                                       //outputs
            //NA comes from retranslator
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"                      //analogInputs
            + "(.*);"                                              //driverKeyCode
            + "((([^:]+:[123]:[^,:]+)(,([^:]+:[123]:[^,:]+))*)|)"; //parameters
    private static final Pattern DATA_PATTERN = compile(DATA_REGEX);

    private static final String NA = "NA";

    private static final int GROUP_NUMBER_HDOP = 28;
    private static final int GROUP_NUMBER_INPUTS = 31;
    private static final int GROUP_NUMBER_OUTPUTS = 33;
    private static final int GROUP_NUMBER_ANALOG_INPUTS = 35;
    private static final int GROUP_NUMBER_DRIVER_KEY_CODE = 40;
    private static final int GROUP_NUMBER_PARAMETERS = 41;

    private static final String NOT_DEFINED_SOURCE = "NA";

    private static final String DELIMITER_ANALOG_INPUTS = ",";
    private static final String DELIMITER_PARAMETERS = ",";

    private final Matcher matcher;
    private final DateParser dateParser;
    private final TimeParser timeParser;
    private final LatitudeParser latitudeParser;
    private final LongitudeParser longitudeParser;
    private final SpeedParser speedParser;
    private final CourseParser courseParser;
    private final AltitudeParser altitudeParser;
    private final AmountOfSatellitesParser amountOfSatellitesParser;

    private final ParameterParser parameterParser;

    public WialonDataComponentsParser(final String data) {
        matcher = DATA_PATTERN.matcher(data);
        match(data);
        dateParser = new DateParser();
        timeParser = new TimeParser();
        latitudeParser = new LatitudeParser();
        longitudeParser = new LongitudeParser();
        speedParser = new SpeedParser();
        courseParser = new CourseParser();
        altitudeParser = new AltitudeParser();
        amountOfSatellitesParser = new AmountOfSatellitesParser();

        parameterParser = new ParameterParser();
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

    public OptionalDouble parseSpeed() {
        return speedParser.parse();
    }

    public OptionalInt parseCourse() {
        return courseParser.parse();
    }

    public OptionalInt parseAltitude() {
        return altitudeParser.parse();
    }

    public OptionalInt parseAmountOfSatellites() {
        return amountOfSatellitesParser.parse();
    }

    public Optional<Double> parseHdop() {
        return parseDouble(GROUP_NUMBER_HDOP);
    }

    public Optional<Integer> parseInputs() {
        return parseInt(GROUP_NUMBER_INPUTS);
    }

    public Optional<Integer> parseOutputs() {
        return parseInt(GROUP_NUMBER_OUTPUTS);
    }

    public Optional<double[]> parseAnalogInputs() {
        return parse(GROUP_NUMBER_ANALOG_INPUTS, this::parseAnalogInputs);
    }

    public Optional<String> parseDriverKeyCode() {
        return parse(GROUP_NUMBER_DRIVER_KEY_CODE, identity());
    }

    public Optional<Set<Parameter>> parseParameters() {
        return parse(GROUP_NUMBER_PARAMETERS, this::parseParameters);
    }

    private void match(final String subMessage) {
        if (!matcher.matches()) {
            throw new NotValidSubMessageException("Given sub message isn't valid: '%s'".formatted(subMessage));
        }
    }

//    private Optional<Double> parseDouble(final int groupNumber) {
//        return parse(groupNumber, Double::valueOf);
//    }

//    private Optional<Integer> parseInt(final int groupNumber) {
//        return parse(groupNumber, Integer::valueOf);
//    }

    private <T> T parse(final int groupNumber, final Function<String, T> parser, final T notDefinedValue) {
        return parse(groupNumber, parser).orElse(notDefinedValue);
    }

    private <T> Optional<T> parse(final int groupNumber, final Function<String, T> parser) {
        final String source = matcher.group(groupNumber);
        return isDefinedSource(source) ? Optional.of(parser.apply(source)) : empty();
    }

//    private OptionalDouble parseDouble(final int groupNumber, final ToDoubleFunction<String> parser) {
//        final String source = matcher.group(groupNumber);
//        return isDefinedSource(source) ? OptionalDouble.of(parser.applyAsDouble(source)) : OptionalDouble.empty();
//    }

    private boolean isDefinedSource(final String source) {
        return !source.isEmpty() && !source.equals(NOT_DEFINED_SOURCE);
    }

    private double[] parseAnalogInputs(final String source) {
        return stream(source.split(DELIMITER_ANALOG_INPUTS))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    private Set<Parameter> parseParameters(final String source) {
        return stream(source.split(DELIMITER_PARAMETERS))
                .map(parameterParser::parse)
                .collect(toUnmodifiableSet());
    }

    @RequiredArgsConstructor
    abstract class ComponentParser<T> {
        private final int groupNumber;
        private final String notDefinedContent;

        public final T parse() {
            final String content = matcher.group(groupNumber);
            return !Objects.equals(content, notDefinedContent) ? parseDefined(content) : createNotDefinedComponent();
        }

        protected abstract T parseDefined(final String content);

        protected abstract T createNotDefinedComponent();
    }

    final class DateParser extends ComponentParser<Optional<LocalDate>> {
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

    final class TimeParser extends ComponentParser<Optional<LocalTime>> {
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

    abstract class CoordinateParser extends ComponentParser<OptionalDouble> {
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

    final class LatitudeParser extends CoordinateParser {
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

    final class LongitudeParser extends CoordinateParser {
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

    abstract class IntComponentParser extends ComponentParser<OptionalInt> {

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

    abstract class DoubleComponentParser extends ComponentParser<OptionalDouble> {

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

    final class SpeedParser extends DoubleComponentParser {
        private static final int GROUP_NUMBER = 20;

        public SpeedParser() {
            super(GROUP_NUMBER);
        }
    }

    final class CourseParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 22;

        public CourseParser() {
            super(GROUP_NUMBER);
        }
    }

    final class AltitudeParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 24;

        public AltitudeParser() {
            super(GROUP_NUMBER);
        }
    }

    final class AmountOfSatellitesParser extends IntComponentParser {
        private static final int GROUP_NUMBER = 26;

        public AmountOfSatellitesParser() {
            super(GROUP_NUMBER);
        }
    }

    private static final class ParameterParser {
        private static final Map<String, ParameterEntity.Type> TYPES_BY_ALIASES = Map.of(
                "1", INTEGER,
                "2", DOUBLE,
                "3", STRING
        );

        private static final String COMPONENTS_DELIMITER = ":";
        private static final int NAME_INDEX = 0;
        private static final int TYPE_INDEX = 1;
        private static final int VALUE_INDEX = 2;

        public Parameter parse(final String source) {
            final String[] components = source.split(COMPONENTS_DELIMITER);
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
