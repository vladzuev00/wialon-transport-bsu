package by.bsu.wialontransport.protocol.wialon.decoder.data.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.GeographicCoordinate;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere.EAST;
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

    private static final int GROUP_NUMBER_SPEED = 20;
    private static final int GROUP_NUMBER_COURSE = 22;
    private static final int GROUP_NUMBER_ALTITUDE = 24;
    private static final int GROUP_NUMBER_AMOUNT_OF_SATELLITES = 26;
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
    private final ParameterParser parameterParser;

    public WialonDataComponentsParser(final String data) {
        matcher = DATA_PATTERN.matcher(data);
        match(data);
        dateParser = new DateParser();
        timeParser = new TimeParser();

        latitudeParser = new LatitudeParser();
        longitudeParser = new LongitudeParser();
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
        return parseDouble(GROUP_NUMBER_SPEED, Double::parseDouble);
    }

    public Optional<Integer> parseCourse() {
        return parseInt(GROUP_NUMBER_COURSE);
    }

    public Optional<Integer> parseAltitude() {
        return parseInt(GROUP_NUMBER_ALTITUDE);
    }

    public Optional<Integer> parseAmountOfSatellites() {
        return parseInt(GROUP_NUMBER_AMOUNT_OF_SATELLITES);
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

    private Optional<Double> parseDouble(final int groupNumber) {
        return parse(groupNumber, Double::valueOf);
    }

    private Optional<Integer> parseInt(final int groupNumber) {
        return parse(groupNumber, Integer::valueOf);
    }

    private <T> T parse(final int groupNumber, final Function<String, T> parser, final T notDefinedValue) {
        return parse(groupNumber, parser).orElse(notDefinedValue);
    }

    private <T> Optional<T> parse(final int groupNumber, final Function<String, T> parser) {
        final String source = matcher.group(groupNumber);
        return isDefinedSource(source) ? Optional.of(parser.apply(source)) : empty();
    }

    private OptionalDouble parseDouble(final int groupNumber, final ToDoubleFunction<String> parser) {
        final String source = matcher.group(groupNumber);
        return isDefinedSource(source) ? OptionalDouble.of(parser.applyAsDouble(source)) : OptionalDouble.empty();
    }

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
        private final int contentGroupNumber;
        private final String notDefinedContent;

        public final T parse() {
            final String content = matcher.group(contentGroupNumber);
            return !Objects.equals(content, notDefinedContent) ? parseDefined(content) : createNotDefinedComponent();
        }

        protected abstract T parseDefined(final String content);

        protected abstract T createNotDefinedComponent();
    }

    class DateParser extends ComponentParser<Optional<LocalDate>> {
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
            return Optional.of(LocalDate.now());
        }
    }

    class TimeParser extends ComponentParser<Optional<LocalTime>> {
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
            return Optional.of(LocalTime.now());
        }
    }



    @RequiredArgsConstructor
    abstract class TempGeographicCoordinateParser<T extends GeographicCoordinate> {
        private static final String NOT_DEFINED_SOURCE = "NA;NA";
        static final int NOT_DEFINED_DEGREES = Integer.MIN_VALUE;
        static final int NOT_DEFINED_MINUTES = Integer.MIN_VALUE;
        static final int NOT_DEFINED_MINUTE_SHARE = Integer.MIN_VALUE;

        private final int groupNumber;
        private final int groupNumberDegrees;
        private final int groupNumberMinutes;
        private final int groupNumberMinuteShare;
        private final int groupNumberType;

        public final OptionalDouble parse() {
            final String source = matcher.group(groupNumber);
            return !source.equals(NOT_DEFINED_SOURCE) ? createDefinedCoordinate() : createNotDefinedCoordinate();
        }

        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char hemisphereValue);

        @SuppressWarnings("SameParameterValue")
        protected abstract T createNotDefinedCoordinate(final int degrees, final int minutes, final int minuteShare);

        private T createDefinedCoordinate() {
            final int degrees = extractGroupAsInteger(groupNumberDegrees);
            final int minutes = extractGroupAsInteger(groupNumberMinutes);
            final int minuteShare = extractGroupAsInteger(groupNumberMinuteShare);
            final char hemisphereValue = extractGroupAsChar(groupNumberType);
            return create(degrees, minutes, minuteShare, hemisphereValue);
        }

        private int extractGroupAsInteger(final int groupNumber) {
            return Integer.parseInt(matcher.group(groupNumber));
        }

        private char extractGroupAsChar(final int groupNumber) {
            return matcher.group(groupNumber).charAt(0);
        }

        private T createNotDefinedCoordinate() {
            return createNotDefinedCoordinate(NOT_DEFINED_DEGREES, NOT_DEFINED_MINUTES, NOT_DEFINED_MINUTE_SHARE);
        }
    }

    final class LatitudeParser extends TempGeographicCoordinateParser<Latitude> {
        private static final int GROUP_NUMBER_LATITUDE = 6;
        private static final int GROUP_NUMBER_LATITUDE_DEGREES = 8;
        private static final int GROUP_NUMBER_LATITUDE_MINUTES = 9;
        private static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 10;
        private static final int GROUP_NUMBER_LATITUDE_TYPE_VALUE = 11;
        static final LatitudeHemisphere NOT_DEFINED_TYPE = NORTH;

        public LatitudeParser() {
            super(
                    GROUP_NUMBER_LATITUDE,
                    GROUP_NUMBER_LATITUDE_DEGREES,
                    GROUP_NUMBER_LATITUDE_MINUTES,
                    GROUP_NUMBER_LATITUDE_MINUTE_SHARE,
                    GROUP_NUMBER_LATITUDE_TYPE_VALUE
            );
        }

        @Override
        protected Latitude create(final int degrees, final int minutes, final int minuteShare, final char hemisphereValue) {
            final LatitudeHemisphere hemisphere = LatitudeHemisphere.findByValue(hemisphereValue);
            return new Latitude(degrees, minutes, minuteShare, hemisphere);
        }

        @Override
        protected Latitude createNotDefinedCoordinate(final int degrees, final int minutes, final int minuteShare) {
            return new Latitude(degrees, minutes, minuteShare, NOT_DEFINED_TYPE);
        }
    }

    final class LongitudeParser extends TempGeographicCoordinateParser<Longitude> {
        private static final int GROUP_NUMBER_LONGITUDE = 13;
        private static final int GROUP_NUMBER_LONGITUDE_DEGREES = 15;
        private static final int GROUP_NUMBER_LONGITUDE_MINUTES = 16;
        private static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 17;
        private static final int GROUP_NUMBER_LONGITUDE_TYPE_VALUE = 18;
        static final LongitudeHemisphere NOT_DEFINED_TYPE = EAST;

        public LongitudeParser() {
            super(
                    GROUP_NUMBER_LONGITUDE,
                    GROUP_NUMBER_LONGITUDE_DEGREES,
                    GROUP_NUMBER_LONGITUDE_MINUTES,
                    GROUP_NUMBER_LONGITUDE_MINUTE_SHARE,
                    GROUP_NUMBER_LONGITUDE_TYPE_VALUE
            );
        }

        @Override
        protected Longitude create(final int degrees, final int minutes, final int minuteShare, final char hemisphereValue) {
            final LongitudeHemisphere hemisphere = LongitudeHemisphere.findByValue(hemisphereValue);
            return new Longitude(degrees, minutes, minuteShare, hemisphere);
        }

        @Override
        protected Longitude createNotDefinedCoordinate(final int degrees, final int minutes, final int minuteShare) {
            return new Longitude(degrees, minutes, minuteShare, NOT_DEFINED_TYPE);
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
