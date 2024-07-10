package by.bsu.wialontransport.util.coordinate;

import lombok.experimental.UtilityClass;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.String.format;
import static java.lang.String.join;

@UtilityClass
public final class NewWingCoordinateUtil {
    private static final String DECIMAL_DELIMITER = ".";

    private static final String LATITUDE_INTEGER_PART_TEMPLATE = "%04d";
    private static final int LATITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 2;

    private static final String LONGITUDE_INTEGER_PART_TEMPLATE = "%05d";
    private static final int LONGITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 3;

    public static double createLatitude(final int integerPart, final int fractionalPart) {
        return calculateGpsCoordinate(
                integerPart,
                fractionalPart,
                LATITUDE_INTEGER_PART_TEMPLATE,
                LATITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX
        );
    }

    public static double createLongitude(final int integerPart, final int fractionalPart) {
        return calculateGpsCoordinate(
                integerPart,
                fractionalPart,
                LONGITUDE_INTEGER_PART_TEMPLATE,
                LONGITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX
        );
    }

    private static double calculateGpsCoordinate(final int integerPart,
                                                 final int fractionalPart,
                                                 final String integerPartTemplate,
                                                 final int firstPartIntegerPartNextLastIndex) {
        final String fractionalPartAsString = Integer.toString(fractionalPart);
        final int absIntegerPart = abs(integerPart);
        final String integerPartAsString = format(integerPartTemplate, absIntegerPart);
        final double absGpsCoordinate =
                parseInt(integerPartAsString.substring(0, firstPartIntegerPartNextLastIndex))
                        +
                        parseFloat(
                                join(
                                        DECIMAL_DELIMITER,
                                        integerPartAsString.substring(integerPartAsString.length() - 2),
                                        fractionalPartAsString.substring(1)
                                )
                        ) / 60;
        return integerPart >= 0 ? absGpsCoordinate : -1 * absGpsCoordinate;
    }
}
