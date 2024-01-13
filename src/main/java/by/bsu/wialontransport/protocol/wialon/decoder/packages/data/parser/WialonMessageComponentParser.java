package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.GeographicCoordinate;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeType.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeType.EAST;
import static java.lang.Byte.parseByte;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;

public final class WialonMessageComponentParser {
    private static final String MESSAGE_REGEX
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"                     //date, time
            + "(((\\d{2})(\\d{2})\\.(\\d+);([NS]))|(NA;NA));"      //latitude
            + "(((\\d{3})(\\d{2})\\.(\\d+);([EW]))|(NA;NA));"      //longitude
            + "(\\d+|(NA));"                                       //speed
            + "(\\d+|(NA));"                                       //course
            + "(\\d+|(NA));"                                       //altitude
            + "(\\d+|(NA));"                                       //amountSatellite
            + "((\\d+\\.\\d+)|(NA));"                              //hdop
            + "(\\d+|(NA));"                                       //inputs
            + "(\\d+|(NA));"                                       //outputs
            //NA comes from retranslator
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"                      //analogInputs
            + "(.*);"                                              //driverKeyCode
            + "((([^:]+:[123]:[^,:]+)(,([^:]+:[123]:[^,:]+))*)|)"; //parameters
    private static final Pattern MESSAGE_PATTERN = compile(MESSAGE_REGEX);

    private static final int GROUP_NUMBER_DATE = 2;
    private static final int GROUP_NUMBER_TIME = 3;

    private static final int GROUP_NUMBER_SPEED = 20;
    private static final int GROUP_NUMBER_COURSE = 22;
    private static final int GROUP_NUMBER_ALTITUDE = 24;
    private static final int GROUP_NUMBER_AMOUNT_OF_SATELLITES = 26;

    private static final String DATE_FORMAT = "ddMMyy";
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern(DATE_FORMAT);
    private static final String NOT_DEFINED_DATE_SOURCE = "NA";
    private static final LocalDate NOT_DEFINED_DATE = LocalDate.MIN;

    private static final String TIME_FORMAT = "HHmmss";
    private static final DateTimeFormatter TIME_FORMATTER = ofPattern(TIME_FORMAT);
    private static final String NOT_DEFINED_TIME_SOURCE = "NA";
    private static final LocalTime NOT_DEFINED_TIME = LocalTime.MIN;


    private static final String NOT_DEFINED_SPEED_SOURCE = "NA";
    private static final int NOT_DEFINED_SPEED = MIN_VALUE;

    private static final String NOT_DEFINED_COURSE_SOURCE = "NA";
    private static final int NOT_DEFINED_COURSE = MIN_VALUE;

    private static final String NOT_DEFINED_ALTITUDE_SOURCE = "NA";
    private static final int NOT_DEFINED_ALTITUDE = MIN_VALUE;

    private static final String NOT_DEFINED_AMOUNT_OF_SATELLITES_SOURCE = "NA";
    private static final int NOT_DEFINED_AMOUNT_OF_SATELLITES = MIN_VALUE;

    private static final int GROUP_NUMBER_HDOP = 28;
    private static final int GROUP_NUMBER_INPUTS = 31;
    private static final int GROUP_NUMBER_OUTPUTS = 33;
    private static final int GROUP_NUMBER_ANALOG_INPUTS = 35;
    private static final int GROUP_NUMBER_DRIVER_KEY_CODE = 40;
    private static final int GROUP_NUMBER_PARAMETERS = 41;

    private static final String NOT_DEFINED_HDOP_SOURCE = "NA";
    private static final Double NOT_DEFINED_HDOP = Double.MIN_VALUE;

    private static final String NOT_DEFINED_INPUTS_SOURCE = "NA";
    private static final int NOT_DEFINED_INPUTS = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_OUTPUTS_SOURCE = "NA";
    private static final int NOT_DEFINED_OUTPUTS = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_ANALOG_INPUTS_SOURCE = "NA";
    private static final String DELIMITER_ANALOG_INPUTS = ",";

    private static final String NOT_DEFINED_DRIVER_KEY_CODE_INBOUND_STRING = "NA";
    private static final String NOT_DEFINED_DRIVER_KEY_CODE = "not defined";

    private static final String DELIMITER_PARAMETERS = ",";

    private final Matcher matcher;
    private final LatitudeParser latitudeParser;
    private final LongitudeParser longitudeParser;

    public WialonMessageComponentParser(final String message) {
        matcher = MESSAGE_PATTERN.matcher(message);
        match(message);
        latitudeParser = new LatitudeParser();
        longitudeParser = new LongitudeParser();
    }

    public LocalDate parseDate() {
        final String source = matcher.group(GROUP_NUMBER_DATE);
        return !source.equals(NOT_DEFINED_DATE_SOURCE) ? LocalDate.parse(source, DATE_FORMATTER) : NOT_DEFINED_DATE;
    }

    public LocalTime parseTime() {
        final String source = matcher.group(GROUP_NUMBER_TIME);
        return !source.equals(NOT_DEFINED_TIME_SOURCE) ? LocalTime.parse(source, TIME_FORMATTER) : NOT_DEFINED_TIME;
    }

    public Latitude parseLatitude() {
        return latitudeParser.parse();
    }

    public Longitude parseLongitude() {
        return longitudeParser.parse();
    }

    public int parseSpeed() {
        final String source = matcher.group(GROUP_NUMBER_SPEED);
        return !source.equals(NOT_DEFINED_SPEED_SOURCE) ? parseInt(source) : NOT_DEFINED_SPEED;
    }

    public int parseCourse() {
        final String source = matcher.group(GROUP_NUMBER_COURSE);
        return !source.equals(NOT_DEFINED_COURSE_SOURCE) ? parseInt(source) : NOT_DEFINED_COURSE;
    }

    public int parseAltitude() {
        final String source = matcher.group(GROUP_NUMBER_ALTITUDE);
        return !source.equals(NOT_DEFINED_ALTITUDE_SOURCE) ? parseInt(source) : NOT_DEFINED_ALTITUDE;
    }

    public int parseAmountOfSatellites() {
        final String source = matcher.group(GROUP_NUMBER_AMOUNT_OF_SATELLITES);
        return !source.equals(NOT_DEFINED_AMOUNT_OF_SATELLITES_SOURCE)
                ? parseInt(source)
                : NOT_DEFINED_AMOUNT_OF_SATELLITES;
    }

    public double parseHdop() {
        final String source = matcher.group(GROUP_NUMBER_HDOP);
        return !source.equals(NOT_DEFINED_HDOP_SOURCE) ? parseDouble(source) : NOT_DEFINED_HDOP;
    }

    public int parseInputs() {
        final String source = matcher.group(GROUP_NUMBER_INPUTS);
        return !source.equals(NOT_DEFINED_INPUTS_SOURCE) ? parseInt(source) : NOT_DEFINED_INPUTS;
    }

    public int parseOutputs() {
        final String source = matcher.group(GROUP_NUMBER_OUTPUTS);
        return !source.equals(NOT_DEFINED_OUTPUTS_SOURCE) ? parseInt(source) : NOT_DEFINED_OUTPUTS;
    }

    public double[] parseAnalogInputs() {
        final String source = matcher.group(GROUP_NUMBER_ANALOG_INPUTS);
        if (source.isEmpty() || source.equals(NOT_DEFINED_ANALOG_INPUTS_SOURCE)) {
            return new double[0];
        }
        return stream(source.split(DELIMITER_ANALOG_INPUTS))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    public String parseDriverKeyCode() {
        final String inboundDriverKeyCode = this.matcher.group(GROUP_NUMBER_DRIVER_KEY_CODE);
        return !inboundDriverKeyCode.equals(NOT_DEFINED_DRIVER_KEY_CODE_INBOUND_STRING)
                ? inboundDriverKeyCode
                : NOT_DEFINED_DRIVER_KEY_CODE;
    }

    public Map<String, Parameter> parseParameters() {
//        final String parametersString = this.matcher.group(GROUP_NUMBER_PARAMETERS);
//        return !parametersString.isEmpty() ?
//                stream(parametersString.split(DELIMITER_PARAMETERS))
//                        .map(this.parameterParser::parse)
//                        .collect(toMap(Parameter::getName, identity()))
//                : emptyMap();
        return null;
    }

    private void match(final String message) {
        if (!matcher.matches()) {
            throw new NotValidMessageException(format("Given message isn't valid: '%s'", message));
        }
    }

    @RequiredArgsConstructor
    private abstract class GeographicCoordinateParser<T extends GeographicCoordinate> {
        private static final String NOT_DEFINED_COORDINATE_STRING = "NA;NA";
        private static final int NOT_DEFINED_DEGREES = MIN_VALUE;
        private static final int NOT_DEFINED_MINUTES = MIN_VALUE;
        private static final int NOT_DEFINED_MINUTE_SHARE = MIN_VALUE;

        private final int groupNumber;
        private final int groupNumberDegrees;
        private final int groupNumberMinutes;
        private final int groupNumberMinuteShare;
        private final int groupNumberType;

        public final T parse() {
            final String source = matcher.group(groupNumber);
            return !source.equals(NOT_DEFINED_COORDINATE_STRING)
                    ? createDefinedCoordinate()
                    : createNotDefinedCoordinate();
        }

        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char typeValue);

        @SuppressWarnings("SameParameterValue")
        protected abstract T createNotDefinedCoordinate(final int degrees, final int minutes, final int minuteShare);

        private T createDefinedCoordinate() {
            final int degrees = extractGroupAsInteger(groupNumberDegrees);
            final int minutes = extractGroupAsInteger(groupNumberMinutes);
            final int minuteShare = extractGroupAsInteger(groupNumberMinuteShare);
            final char typeValue = extractGroupAsChar(groupNumberType);
            return create(degrees, minutes, minuteShare, typeValue);
        }

        private int extractGroupAsInteger(final int groupNumber) {
            return parseInt(matcher.group(groupNumber));
        }

        private char extractGroupAsChar(final int groupNumber) {
            return matcher.group(groupNumber).charAt(0);
        }

        private T createNotDefinedCoordinate() {
            return createNotDefinedCoordinate(NOT_DEFINED_DEGREES, NOT_DEFINED_MINUTES, NOT_DEFINED_MINUTE_SHARE);
        }
    }

    private final class LatitudeParser extends GeographicCoordinateParser<Latitude> {
        private static final int GROUP_NUMBER_LATITUDE = 4;
        private static final int GROUP_NUMBER_LATITUDE_DEGREES = 8;
        private static final int GROUP_NUMBER_LATITUDE_MINUTES = 9;
        private static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 10;
        private static final int GROUP_NUMBER_LATITUDE_TYPE_VALUE = 11;
        private static final LatitudeType NOT_DEFINED_TYPE = NORTH;

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
        protected Latitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final LatitudeType type = LatitudeType.findByValue(typeValue);
            return new Latitude(degrees, minutes, minuteShare, type);
        }

        @Override
        protected Latitude createNotDefinedCoordinate(final int degrees, final int minutes, final int minuteShare) {
            return new Latitude(degrees, minutes, minuteShare, NOT_DEFINED_TYPE);
        }
    }

    private final class LongitudeParser extends GeographicCoordinateParser<Longitude> {
        private static final int GROUP_NUMBER_LONGITUDE = 12;
        private static final int GROUP_NUMBER_LONGITUDE_DEGREES = 15;
        private static final int GROUP_NUMBER_LONGITUDE_MINUTES = 16;
        private static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 17;
        private static final int GROUP_NUMBER_LONGITUDE_TYPE_VALUE = 18;
        private static final LongitudeType NOT_DEFINED_TYPE = EAST;

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
        protected Longitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final LongitudeType type = LongitudeType.findByValue(typeValue);
            return new Longitude(degrees, minutes, minuteShare, type);
        }

        @Override
        protected Longitude createNotDefinedCoordinate(final int degrees, final int minutes, final int minuteShare) {
            return new Longitude(degrees, minutes, minuteShare, NOT_DEFINED_TYPE);
        }
    }

    private static final class ParameterParser {
        private static final String COMPONENTS_DELIMITER = ":";
        private static final int NAME_INDEX = 0;
        private static final int TYPE_INDEX = 1;
        private static final int VALUE_INDEX = 2;

        public Parameter parse(final String source) {
            final String[] components = source.split(COMPONENTS_DELIMITER);
            final String name = components[NAME_INDEX];
            final ParameterEntity.Type type = parseType(components[TYPE_INDEX]);
            final String value = components[VALUE_INDEX];
            return Parameter.builder()
                    .name(name)
                    .type(type)
                    .value(value)
                    .build();
        }

        private static ParameterEntity.Type parseType(final String source) {
            final byte value = parseByte(source);
            return ParameterEntity.Type.findByValue(value);
        }
    }
}
