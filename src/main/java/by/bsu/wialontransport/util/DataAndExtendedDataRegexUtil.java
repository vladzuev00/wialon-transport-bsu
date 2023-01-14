package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@UtilityClass
public class DataAndExtendedDataRegexUtil {
    public static final String REGEX_DATA
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"                     //date, time
            + "(((\\d{2})(\\d{2})\\.(\\d+);([NS]))|(NA;NA));"      //latitude
            + "(((\\d{3})(\\d{2})\\.(\\d+);([EW]))|(NA;NA));"      //longitude
            + "(\\d+|(NA));"                                       //speed
            + "(\\d+|(NA));"                                       //course
            + "(\\d+|(NA));"                                       //altitude
            + "(\\d+|(NA));";                                      //amountSatellite
    public static final Pattern PATTERN_DATA = compile(REGEX_DATA);

    public static final String REGEX_EXTENDED_DATA = REGEX_DATA
            + "((\\d+\\.\\d+)|(NA));"                              //hdop
            + "(\\d+|(NA));"                                       //inputs
            + "(\\d+|(NA));"                                       //outputs
            //NA comes from retranslator
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"                      //analogInputs
            + "(.*);"                                              //driverKeyCode
            + "(([^:]+:[123]:[^:]+,?)*)";                          //parameters;
    public static final Pattern PATTERN_EXTENDED_DATA = compile(REGEX_EXTENDED_DATA);

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

    //Numbers of groups of additional properties for extended data:
    public static final int GROUP_NUMBER_REDUCTION_PRECISION = 28;
    public static final int GROUP_NUMBER_INPUTS = 31;
    public static final int GROUP_NUMBER_OUTPUTS = 33;
    public static final int GROUP_NUMBER_ANALOG_INPUTS = 35;
    public static final int GROUP_NUMBER_DRIVER_KEY_CODE = 40;
    public static final int GROUP_NUMBER_PARAMETERS = 41;
}
