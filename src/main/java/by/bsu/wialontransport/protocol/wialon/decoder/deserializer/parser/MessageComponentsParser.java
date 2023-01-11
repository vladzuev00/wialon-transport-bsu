package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Data.GeographicCoordinate;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.regex.Pattern.compile;

public final class MessageComponentsParser {
    private static final String MESSAGE_TEMPLATE_EXCEPTION_NOT_VALID_MESSAGE = "Given message '%s' isn't valid.";

    private static final int GROUP_NUMBER_DATE_TIME = 1;

    private static final int GROUP_NUMBER_LATITUDE_DEGREES = 6;
    private static final int GROUP_NUMBER_LATITUDE_MINUTES = 7;
    private static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 8;
    private static final int GROUP_NUMBER_LATITUDE_TYPE_VALUE = 9;

    private static final int GROUP_NUMBER_LONGITUDE_DEGREES = 10;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTES = 11;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 12;
    private static final int GROUP_NUMBER_LONGITUDE_TYPE_VALUE = 13;

    private static final int GROUP_NUMBER_SPEED = 14;
    private static final int GROUP_NUMBER_COURSE = 16;
    private static final int GROUP_NUMBER_ALTITUDE = 18;
    private static final int GROUP_NUMBER_AMOUNT_SATELLITES = 20;
    private static final int GROUP_NUMBER_PARAMETERS = 35;

    private static final String DATE_TIME_FORMAT = "ddMMyy;HHmmss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_FORMAT);
    private static final String NOT_DEFINED_DATE_TIME_STRING = "NA;NA";
    private static final LocalDateTime NOT_DEFINED_DATE_TIME = LocalDateTime.MIN;

    private static final String NOT_DEFINE_SPEED_STRING = "NA";
    private static final int NOT_DEFINED_SPEED = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_COURSE_STRING = "NA";
    private static final int NOT_DEFINED_COURSE = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_ALTITUDE_STRING = "NA";
    private static final int NOT_DEFINED_ALTITUDE = 0;

    private static final String NOT_DEFINED_AMOUNT_SATELLITE_STRING = "NA";
    private static final int NOT_DEFINED_AMOUNT_SATELLITE = 0;

    private static final String DELIMITER_PARAMETERS = ",";
    private static final String DELIMITER_PARAMETER_COMPONENTS = ":";

    private static final String MESSAGE_REGEX
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"                     //date, time
            + "(((\\d{2})(\\d{2})\\.(\\d+);([NS]))|(NA;NA));"      //latitude
            + "(((\\d{3})(\\d{2})\\.(\\d+);([EW]))|(NA;NA));"      //longitude
            + "(\\d+|(NA));"                                       //speed
            + "(\\d+|(NA));"                                       //course
            + "(\\d+|(NA));"                                       //altitude
            + "(\\d+|(NA));"                                       //amountSatellite
            + "((\\d+\\.\\d+)|(NA));"                              //hdop
            + "(((\\d+|(NA));){2})"                                //inputs, outputs
            //NA comes from retranslator
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"                      //analogInputs
            + "(.*);"                                              //driverKeyCode
            + "(([^:]+:[123]:[^:]+,?)*)";                          //parameters
    private static final Pattern MESSAGE_PATTERN = compile(MESSAGE_REGEX);

    private final Matcher matcher;
    private final GeographicCoordinateParser<Latitude> latitudeParser;
    private final GeographicCoordinateParser<Longitude> longitudeParser;

    public MessageComponentsParser(final String source) {
        this.matcher = MESSAGE_PATTERN.matcher(source);
        if (!this.matcher.matches()) {
            throw new NotValidMessageException(format(MESSAGE_TEMPLATE_EXCEPTION_NOT_VALID_MESSAGE, source));
        }
        this.latitudeParser = new LatitudeParser();
        this.longitudeParser = new LongitudeParser();
    }

    public LocalDateTime parseDateTime() {
        final String dateTimeGroup = this.matcher.group(GROUP_NUMBER_DATE_TIME);
        return !dateTimeGroup.equals(NOT_DEFINED_DATE_TIME_STRING)
                ? parse(dateTimeGroup, DATE_TIME_FORMATTER)
                : NOT_DEFINED_DATE_TIME;
    }

    public Latitude parseLatitude() {
        return this.latitudeParser.parse();
    }

    public Longitude parseLongitude() {
        return this.longitudeParser.parse();
    }

    public int parseSpeed() {
        final String speedString = this.matcher.group(GROUP_NUMBER_SPEED);
        return !speedString.equals(NOT_DEFINE_SPEED_STRING) ? parseInt(speedString) : NOT_DEFINED_SPEED;
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

//    public List<Parameter> parseParameters() {
//        final String parameters = this.matcher.group(GROUP_NUMBER_PARAMETERS);
//        stream(parameters.split(DELIMITER_PARAMETERS))
//                .map(parameterString -> parameterString.split(DELIMITER_PARAMETER_COMPONENTS))
//                .
//    }

    private abstract class GeographicCoordinateParser<T extends GeographicCoordinate> {
        private final int groupNumberDegrees;
        private final int groupNumberMinutes;
        private final int groupNumberMinuteShare;
        private final int groupNumberType;

        public GeographicCoordinateParser(final int groupNumberDegrees, final int groupNumberMinutes,
                                          final int groupNumberMinuteShare, final int groupNumberType) {
            this.groupNumberDegrees = groupNumberDegrees;
            this.groupNumberMinutes = groupNumberMinutes;
            this.groupNumberMinuteShare = groupNumberMinuteShare;
            this.groupNumberType = groupNumberType;
        }

        public final T parse() {
            final int degrees = parseInt(MessageComponentsParser.this.matcher.group(this.groupNumberDegrees));
            final int minutes = parseInt(MessageComponentsParser.this.matcher.group(this.groupNumberMinutes));
            final int minuteShare = parseInt(MessageComponentsParser.this.matcher.group(this.groupNumberMinuteShare));
            final String typeValue = MessageComponentsParser.this.matcher.group(this.groupNumberType);
            return this.create(degrees, minutes, minuteShare, typeValue.charAt(0));
        }

        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char typeValue);
    }

    private final class LatitudeParser extends GeographicCoordinateParser<Latitude> {

        public LatitudeParser() {
            super(GROUP_NUMBER_LATITUDE_DEGREES, GROUP_NUMBER_LATITUDE_MINUTES, GROUP_NUMBER_LATITUDE_MINUTE_SHARE,
                    GROUP_NUMBER_LATITUDE_TYPE_VALUE);
        }

        @Override
        protected Latitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final DataEntity.Latitude.Type type = DataEntity.Latitude.Type.findByValue(typeValue);
            return new Latitude(degrees, minutes, minuteShare, type);
        }
    }

    private final class LongitudeParser extends GeographicCoordinateParser<Longitude> {

        public LongitudeParser() {
            super(GROUP_NUMBER_LONGITUDE_DEGREES, GROUP_NUMBER_LONGITUDE_MINUTES, GROUP_NUMBER_LONGITUDE_MINUTE_SHARE,
                    GROUP_NUMBER_LONGITUDE_TYPE_VALUE);
        }

        @Override
        protected Longitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final DataEntity.Longitude.Type type = DataEntity.Longitude.Type.findByValue(typeValue);
            return new Longitude(degrees, minutes, minuteShare, type);
        }
    }
}
