package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser.exception.NotValidDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Byte.parseByte;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;

public final class DataComponentsParser {
    private static final String MESSAGE_TEMPLATE_NOT_VALID_DATA_EXCEPTION = "Given data '%s' isn't valid.";

    private static final String REGEX_DATA
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
    private static final Pattern PATTERN_DATA = compile(REGEX_DATA);

    private static final int GROUP_NUMBER_DATE_TIME = 1;

    private static final int GROUP_NUMBER_LATITUDE = 6;
    private static final int GROUP_NUMBER_LATITUDE_DEGREES = 8;
    private static final int GROUP_NUMBER_LATITUDE_MINUTES = 9;
    private static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 10;
    private static final int GROUP_NUMBER_LATITUDE_TYPE_VALUE = 11;

    private static final int GROUP_NUMBER_LONGITUDE = 13;
    private static final int GROUP_NUMBER_LONGITUDE_DEGREES = 15;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTES = 16;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 17;
    private static final int GROUP_NUMBER_LONGITUDE_TYPE_VALUE = 18;

    private static final int GROUP_NUMBER_SPEED = 20;
    private static final int GROUP_NUMBER_COURSE = 22;
    private static final int GROUP_NUMBER_ALTITUDE = 24;
    private static final int GROUP_NUMBER_AMOUNT_SATELLITES = 26;

    private static final String DATE_TIME_FORMAT = "ddMMyy;HHmmss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_FORMAT);
    private static final String NOT_DEFINED_DATE_TIME_STRING = "NA;NA";
    private static final LocalDateTime NOT_DEFINED_DATE_TIME = LocalDateTime.MIN;

    private static final String NOT_DEFINED_SPEED_STRING = "NA";
    private static final int NOT_DEFINED_SPEED = MIN_VALUE;

    private static final String NOT_DEFINED_COURSE_STRING = "NA";
    private static final int NOT_DEFINED_COURSE = MIN_VALUE;

    private static final String NOT_DEFINED_ALTITUDE_STRING = "NA";
    private static final int NOT_DEFINED_ALTITUDE = MIN_VALUE;

    private static final String NOT_DEFINED_AMOUNT_SATELLITE_STRING = "NA";
    private static final int NOT_DEFINED_AMOUNT_SATELLITE = MIN_VALUE;

    private static final int GROUP_NUMBER_REDUCTION_PRECISION = 28;
    private static final int GROUP_NUMBER_INPUTS = 31;
    private static final int GROUP_NUMBER_OUTPUTS = 33;
    private static final int GROUP_NUMBER_ANALOG_INPUTS = 35;
    private static final int GROUP_NUMBER_DRIVER_KEY_CODE = 40;
    private static final int GROUP_NUMBER_PARAMETERS = 41;

    private static final String NOT_DEFINED_REDUCTION_PRECISION_STRING = "NA";
    private static final Double NOT_DEFINED_REDUCTION_PRECISION = Double.MIN_VALUE;

    private static final String NOT_DEFINED_INPUTS_STRING = "NA";
    private static final int NOT_DEFINED_INPUTS = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_OUTPUTS_STRING = "NA";
    private static final int NOT_DEFINED_OUTPUTS = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_ANALOG_INPUTS_STRING = "NA";
    private static final String DELIMITER_ANALOG_INPUTS = ",";

    private static final String NOT_DEFINED_DRIVER_KEY_CODE_INBOUND_STRING = "NA";
    private static final String NOT_DEFINED_DRIVER_KEY_CODE = "not defined";

    private static final String DELIMITER_PARAMETERS = ",";

    private final Matcher matcher;
//    private final GeographicCoordinateParser<Latitude> latitudeParser;
//    private final GeographicCoordinateParser<Longitude> longitudeParser;
    private final ParameterParser parameterParser;

    public DataComponentsParser(final String source) {
        this.matcher = PATTERN_DATA.matcher(source);
        if (!this.matcher.matches()) {
            throw new NotValidDataException(format(MESSAGE_TEMPLATE_NOT_VALID_DATA_EXCEPTION, source));
        }
//        this.latitudeParser = new LatitudeParser();
//        this.longitudeParser = new LongitudeParser();
        this.parameterParser = new ParameterParser();
    }

    public LocalDateTime parseDateTime() {
        final String dateTimeGroup = this.matcher.group(GROUP_NUMBER_DATE_TIME);
        return !dateTimeGroup.equals(NOT_DEFINED_DATE_TIME_STRING)
                ? parse(dateTimeGroup, DATE_TIME_FORMATTER)
                : NOT_DEFINED_DATE_TIME;
    }

//    public Latitude parseLatitude() {
//        return this.latitudeParser.parse();
//    }
//
//    public Longitude parseLongitude() {
//        return this.longitudeParser.parse();
//    }

    public int parseSpeed() {
        final String speedString = this.matcher.group(GROUP_NUMBER_SPEED);
        return !speedString.equals(NOT_DEFINED_SPEED_STRING) ? parseInt(speedString) : NOT_DEFINED_SPEED;
    }

    public int parseCourse() {
        final String courseString = this.matcher.group(GROUP_NUMBER_COURSE);
        return !courseString.equals(NOT_DEFINED_COURSE_STRING) ? parseInt(courseString) : NOT_DEFINED_COURSE;
    }

    public int parseAltitude() {
        final String altitudeString = this.matcher.group(GROUP_NUMBER_ALTITUDE);
        return !altitudeString.equals(NOT_DEFINED_ALTITUDE_STRING) ? parseInt(altitudeString) : NOT_DEFINED_ALTITUDE;
    }

    public int parseAmountSatellites() {
        final String amountSatellitesString = this.matcher.group(GROUP_NUMBER_AMOUNT_SATELLITES);
        return !amountSatellitesString.equals(NOT_DEFINED_AMOUNT_SATELLITE_STRING)
                ? parseInt(amountSatellitesString)
                : NOT_DEFINED_AMOUNT_SATELLITE;
    }

    public double parseReductionPrecision() {
        final String reductionPrecisionString = this.matcher.group(GROUP_NUMBER_REDUCTION_PRECISION);
        return !reductionPrecisionString.equals(NOT_DEFINED_REDUCTION_PRECISION_STRING)
                ? parseDouble(reductionPrecisionString)
                : NOT_DEFINED_REDUCTION_PRECISION;
    }

    public int parseInputs() {
        final String inputsString = this.matcher.group(GROUP_NUMBER_INPUTS);
        return !inputsString.equals(NOT_DEFINED_INPUTS_STRING) ? parseInt(inputsString) : NOT_DEFINED_INPUTS;
    }

    public int parseOutputs() {
        final String outputsString = this.matcher.group(GROUP_NUMBER_OUTPUTS);
        return !outputsString.equals(NOT_DEFINED_OUTPUTS_STRING) ? parseInt(outputsString) : NOT_DEFINED_OUTPUTS;
    }

    public double[] parseAnalogInputs() {
        final String analogInputsString = this.matcher.group(GROUP_NUMBER_ANALOG_INPUTS);
        if (analogInputsString.isEmpty() || analogInputsString.equals(NOT_DEFINED_ANALOG_INPUTS_STRING)) {
            return new double[0];
        }
        return stream(analogInputsString.split(DELIMITER_ANALOG_INPUTS))
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
        final String parametersString = this.matcher.group(GROUP_NUMBER_PARAMETERS);
        return !parametersString.isEmpty() ?
                stream(parametersString.split(DELIMITER_PARAMETERS))
                        .map(this.parameterParser::parse)
                        .collect(toMap(Parameter::getName, identity()))
                : emptyMap();
    }

//    private abstract class GeographicCoordinateParser<T extends GeographicCoordinate> {
//        private static final String NOT_DEFINED_GEOGRAPHIC_COORDINATE_STRING = "NA;NA";
//
//        private final int groupNumber;
//        private final int groupNumberDegrees;
//        private final int groupNumberMinutes;
//        private final int groupNumberMinuteShare;
//        private final int groupNumberType;
//
//        public GeographicCoordinateParser(final int groupNumber, final int groupNumberDegrees,
//                                          final int groupNumberMinutes, final int groupNumberMinuteShare,
//                                          final int groupNumberType) {
//            this.groupNumber = groupNumber;
//            this.groupNumberDegrees = groupNumberDegrees;
//            this.groupNumberMinutes = groupNumberMinutes;
//            this.groupNumberMinuteShare = groupNumberMinuteShare;
//            this.groupNumberType = groupNumberType;
//        }
//
//        public final T parse() {
//            final String geographicCoordinateString = DataComponentsParser.this.matcher.group(this.groupNumber);
//            return !geographicCoordinateString.equals(NOT_DEFINED_GEOGRAPHIC_COORDINATE_STRING)
//                    ? this.createDefinedGeographicCoordinate(DataComponentsParser.this.matcher)
//                    : this.createNotDefinedGeographicCoordinate();
//        }
//
//        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char typeValue);
//
//        protected abstract T createNotDefinedGeographicCoordinate();
//
//        private T createDefinedGeographicCoordinate(final Matcher matcher) {
//            final int degrees = parseInt(matcher.group(this.groupNumberDegrees));
//            final int minutes = parseInt(matcher.group(this.groupNumberMinutes));
//            final int minuteShare = parseInt(matcher.group(this.groupNumberMinuteShare));
//            final String typeValue = matcher.group(this.groupNumberType);
//            return this.create(degrees, minutes, minuteShare, typeValue.charAt(0));
//        }
//    }
//
//    private final class LatitudeParser extends GeographicCoordinateParser<Latitude> {
//        private static final Latitude NOT_DEFINED_LATITUDE = Latitude.builder()
//                .degrees(MIN_VALUE)
//                .minutes(MIN_VALUE)
//                .minuteShare(MIN_VALUE)
//                .type(DataEntity.Latitude.Type.NOT_DEFINED)
//                .build();
//
//        public LatitudeParser() {
//            super(GROUP_NUMBER_LATITUDE, GROUP_NUMBER_LATITUDE_DEGREES, GROUP_NUMBER_LATITUDE_MINUTES,
//                    GROUP_NUMBER_LATITUDE_MINUTE_SHARE, GROUP_NUMBER_LATITUDE_TYPE_VALUE);
//        }
//
//        @Override
//        protected Latitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
//            final DataEntity.Latitude.Type type = DataEntity.Latitude.Type.findByValue(typeValue);
//            return new Latitude(degrees, minutes, minuteShare, type);
//        }
//
//        @Override
//        protected Latitude createNotDefinedGeographicCoordinate() {
//            return NOT_DEFINED_LATITUDE;
//        }
//    }
//
//    private final class LongitudeParser extends GeographicCoordinateParser<Longitude> {
//        private static final Longitude NOT_DEFINED_LONGITUDE = Longitude.builder()
//                .degrees(MIN_VALUE)
//                .minutes(MIN_VALUE)
//                .minuteShare(MIN_VALUE)
//                .type(DataEntity.Longitude.Type.NOT_DEFINED)
//                .build();
//
//        public LongitudeParser() {
//            super(GROUP_NUMBER_LONGITUDE, GROUP_NUMBER_LONGITUDE_DEGREES, GROUP_NUMBER_LONGITUDE_MINUTES,
//                    GROUP_NUMBER_LONGITUDE_MINUTE_SHARE, GROUP_NUMBER_LONGITUDE_TYPE_VALUE);
//        }
//
//        @Override
//        protected Longitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
//            final DataEntity.Longitude.Type type = DataEntity.Longitude.Type.findByValue(typeValue);
//            return new Longitude(degrees, minutes, minuteShare, type);
//        }
//
//        @Override
//        protected Longitude createNotDefinedGeographicCoordinate() {
//            return NOT_DEFINED_LONGITUDE;
//        }
//    }

    private static final class ParameterParser {
        private static final String DELIMITER_PARAMETER_COMPONENTS = ":";
        private static final int PARAMETER_NAME_INDEX = 0;
        private static final int PARAMETER_TYPE_INDEX = 1;
        private static final int PARAMETER_VALUE_INDEX = 2;

        public Parameter parse(final String source) {
            final String[] components = source.split(DELIMITER_PARAMETER_COMPONENTS);
            final String name = components[PARAMETER_NAME_INDEX];
            final ParameterEntity.Type type = parseType(components);
            final String value = components[PARAMETER_VALUE_INDEX];
            return Parameter.builder()
                    .name(name)
                    .type(type)
                    .value(value)
                    .build();
        }

        private static ParameterEntity.Type parseType(final String[] components) {
            final String typeString = components[PARAMETER_TYPE_INDEX];
            return ParameterEntity.Type.findByValue(parseByte(typeString));
        }
    }
}
