package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components;

import by.bsu.wialontransport.crud.dto.Data.GeographicCoordinate;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.entity.DataEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.regex.Pattern.compile;

public class DataComponentsParser {
    protected static final String REGEX_DATA
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"                     //date, time
            + "(((\\d{2})(\\d{2})\\.(\\d+);([NS]))|(NA;NA));"      //latitude
            + "(((\\d{3})(\\d{2})\\.(\\d+);([EW]))|(NA;NA));"      //longitude
            + "(\\d+|(NA));"                                       //speed
            + "(\\d+|(NA));"                                       //course
            + "(\\d+|(NA));"                                       //altitude
            + "(\\d+|(NA));";                                      //amountSatellite
    public static final Pattern PATTERN_DATA = compile(REGEX_DATA);

    public static final int GROUP_NUMBER_DATE_TIME = 1;

    public static final int GROUP_NUMBER_LATITUDE = 6;
    public static final int GROUP_NUMBER_LATITUDE_DEGREES = 8;
    public static final int GROUP_NUMBER_LATITUDE_MINUTES = 9;
    public static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 10;
    public static final int GROUP_NUMBER_LATITUDE_TYPE_VALUE = 11;

    public static final int GROUP_NUMBER_LONGITUDE = 13;
    public static final int GROUP_NUMBER_LONGITUDE_DEGREES = 15;
    public static final int GROUP_NUMBER_LONGITUDE_MINUTES = 16;
    public static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 17;
    public static final int GROUP_NUMBER_LONGITUDE_TYPE_VALUE = 18;

    public static final int GROUP_NUMBER_SPEED = 20;
    public static final int GROUP_NUMBER_COURSE = 22;
    public static final int GROUP_NUMBER_ALTITUDE = 24;
    public static final int GROUP_NUMBER_AMOUNT_SATELLITES = 26;

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

    private final GeographicCoordinateParser<Latitude> latitudeParser;
    private final GeographicCoordinateParser<Longitude> longitudeParser;
    private Matcher matcher;

    public DataComponentsParser() {
        this.latitudeParser = new LatitudeParser();
        this.longitudeParser = new LongitudeParser();
        this.matcher = null;
    }

    public final boolean match(final String source) {
        final Pattern pattern = this.findPattern();
        this.matcher = pattern.matcher(source);
        return this.matcher.matches();
    }

    public final LocalDateTime parseDateTime() {
        final String dateTimeGroup = this.matcher.group(GROUP_NUMBER_DATE_TIME);
        return !dateTimeGroup.equals(NOT_DEFINED_DATE_TIME_STRING)
                ? parse(dateTimeGroup, DATE_TIME_FORMATTER)
                : NOT_DEFINED_DATE_TIME;
    }

    public final Latitude parseLatitude() {
        return this.latitudeParser.parse();
    }

    public final Longitude parseLongitude() {
        return this.longitudeParser.parse();
    }

    public final int parseSpeed() {
        final String speedString = this.matcher.group(GROUP_NUMBER_SPEED);
        return !speedString.equals(NOT_DEFINED_SPEED_STRING) ? parseInt(speedString) : NOT_DEFINED_SPEED;
    }

    public final int parseCourse() {
        final String courseString = this.matcher.group(GROUP_NUMBER_COURSE);
        return !courseString.equals(NOT_DEFINED_COURSE_STRING) ? parseInt(courseString) : NOT_DEFINED_COURSE;
    }

    public final int parseAltitude() {
        final String altitudeString = this.matcher.group(GROUP_NUMBER_ALTITUDE);
        return !altitudeString.equals(NOT_DEFINED_ALTITUDE_STRING) ? parseInt(altitudeString) : NOT_DEFINED_ALTITUDE;
    }

    public final int parseAmountSatellites() {
        final String amountSatellitesString = this.matcher.group(GROUP_NUMBER_AMOUNT_SATELLITES);
        return !amountSatellitesString.equals(NOT_DEFINED_AMOUNT_SATELLITE_STRING)
                ? parseInt(amountSatellitesString)
                : NOT_DEFINED_AMOUNT_SATELLITE;
    }

    protected final Matcher getMatcher() {
        return this.matcher;
    }

    protected Pattern findPattern() {
        return PATTERN_DATA;
    }

    private abstract class GeographicCoordinateParser<T extends GeographicCoordinate> {
        private static final String NOT_DEFINED_GEOGRAPHIC_COORDINATE_STRING = "NA;NA";

        private final int groupNumber;
        private final int groupNumberDegrees;
        private final int groupNumberMinutes;
        private final int groupNumberMinuteShare;
        private final int groupNumberType;

        public GeographicCoordinateParser(final int groupNumber, final int groupNumberDegrees,
                                          final int groupNumberMinutes, final int groupNumberMinuteShare,
                                          final int groupNumberType) {
            this.groupNumber = groupNumber;
            this.groupNumberDegrees = groupNumberDegrees;
            this.groupNumberMinutes = groupNumberMinutes;
            this.groupNumberMinuteShare = groupNumberMinuteShare;
            this.groupNumberType = groupNumberType;
        }

        public final T parse() {
            final String geographicCoordinateString = DataComponentsParser.this.matcher.group(this.groupNumber);
            return !geographicCoordinateString.equals(NOT_DEFINED_GEOGRAPHIC_COORDINATE_STRING)
                    ? this.createDefinedGeographicCoordinate()
                    : this.createNotDefinedGeographicCoordinate();
        }

        protected abstract T create(final int degrees, final int minutes, final int minuteShare, final char typeValue);

        protected abstract T createNotDefinedGeographicCoordinate();

        private T createDefinedGeographicCoordinate() {
            final int degrees = parseInt(DataComponentsParser.this.matcher.group(this.groupNumberDegrees));
            final int minutes = parseInt(DataComponentsParser.this.matcher.group(this.groupNumberMinutes));
            final int minuteShare = parseInt(
                    DataComponentsParser.this.matcher.group(this.groupNumberMinuteShare));
            final String typeValue = DataComponentsParser.this.matcher.group(this.groupNumberType);
            return this.create(degrees, minutes, minuteShare, typeValue.charAt(0));
        }
    }

    private final class LatitudeParser extends GeographicCoordinateParser<Latitude> {
        private static final Latitude NOT_DEFINED_LATITUDE = Latitude.builder()
                .degrees(MIN_VALUE)
                .minutes(MIN_VALUE)
                .minuteShare(MIN_VALUE)
                .type(DataEntity.Latitude.Type.NOT_DEFINED)
                .build();

        public LatitudeParser() {
            super(GROUP_NUMBER_LATITUDE, GROUP_NUMBER_LATITUDE_DEGREES, GROUP_NUMBER_LATITUDE_MINUTES,
                    GROUP_NUMBER_LATITUDE_MINUTE_SHARE, GROUP_NUMBER_LATITUDE_TYPE_VALUE);
        }

        @Override
        protected Latitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final DataEntity.Latitude.Type type = DataEntity.Latitude.Type.findByValue(typeValue);
            return new Latitude(degrees, minutes, minuteShare, type);
        }

        @Override
        protected Latitude createNotDefinedGeographicCoordinate() {
            return NOT_DEFINED_LATITUDE;
        }
    }

    private final class LongitudeParser extends GeographicCoordinateParser<Longitude> {
        private static final Longitude NOT_DEFINED_LONGITUDE = Longitude.builder()
                .degrees(MIN_VALUE)
                .minutes(MIN_VALUE)
                .minuteShare(MIN_VALUE)
                .type(DataEntity.Longitude.Type.NOT_DEFINED)
                .build();

        public LongitudeParser() {
            super(GROUP_NUMBER_LONGITUDE, GROUP_NUMBER_LONGITUDE_DEGREES, GROUP_NUMBER_LONGITUDE_MINUTES,
                    GROUP_NUMBER_LONGITUDE_MINUTE_SHARE, GROUP_NUMBER_LONGITUDE_TYPE_VALUE);
        }

        @Override
        protected Longitude create(final int degrees, final int minutes, final int minuteShare, final char typeValue) {
            final DataEntity.Longitude.Type type = DataEntity.Longitude.Type.findByValue(typeValue);
            return new Longitude(degrees, minutes, minuteShare, type);
        }

        @Override
        protected Longitude createNotDefinedGeographicCoordinate() {
            return NOT_DEFINED_LONGITUDE;
        }
    }
}
