package by.vladzuev.locationreceiver.protocol.newwing.util;

import by.vladzuev.locationreceiver.util.NumberUtil;
import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.String.join;

@UtilityClass
public final class NewWingUtil {
    private static final int YEAR_MARK_POINT = 2000;
    private static final String LATITUDE_INTEGER_PART_TEMPLATE = "%04d";
    private static final int LATITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 2;
    private static final String LONGITUDE_INTEGER_PART_TEMPLATE = "%05d";
    private static final int LONGITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 3;
    private static final String POINT = ".";

    public static short decodeShort(final ByteBuf buffer) {
        return buffer.readShortLE();
    }

    public static String decodeImei(final ByteBuf buffer) {
        final short imei = decodeShort(buffer);
        return Short.toString(imei);
    }

    public static LocalDate decodeDate(final ByteBuf buffer) {
        final byte day = buffer.readByte();
        final byte month = buffer.readByte();
        final byte year = buffer.readByte();
        return LocalDate.of(YEAR_MARK_POINT + year, month, day);
    }

    public static LocalTime decodeTime(final ByteBuf buffer) {
        final byte hour = buffer.readByte();
        final byte minute = buffer.readByte();
        final byte second = buffer.readByte();
        return LocalTime.of(hour, minute, second);
    }

    public static double decodeLatitude(final ByteBuf buffer) {
        return decodeGpsCoordinate(
                buffer,
                LATITUDE_INTEGER_PART_TEMPLATE,
                LATITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX
        );
    }

    public static double decodeLongitude(final ByteBuf buffer) {
        return decodeGpsCoordinate(
                buffer,
                LONGITUDE_INTEGER_PART_TEMPLATE,
                LONGITUDE_FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX
        );
    }

    public static double decodeHdop(final ByteBuf buffer) {
        final byte integerPart = buffer.readByte();
        final byte fractionalPart = buffer.readByte();
        return NumberUtil.concatToDouble(integerPart, fractionalPart);
    }

    public static double decodeSpeed(final ByteBuf buffer) {
        final short integerPart = decodeShort(buffer);
        final byte fractionalPart = buffer.readByte();
        return NumberUtil.concatToDouble(integerPart, fractionalPart);
    }

    public static double[] decodeAnalogInputs(final ByteBuf buffer) {
        final short first = decodeShort(buffer);
        final short second = decodeShort(buffer);
        final short third = decodeShort(buffer);
        final short fourth = decodeShort(buffer);
        return new double[]{first, second, third, fourth};
    }

    private static double decodeGpsCoordinate(final ByteBuf buffer,
                                              final String integerPartTemplate,
                                              final int firstPartIntegerPartNextLastIndex) {
        final short integerPart = decodeShort(buffer);
        final short fractionalPart = decodeShort(buffer);
        final String integerPartAsString = integerPartTemplate.formatted(abs(integerPart));
        final double abs = parseInt(integerPartAsString.substring(0, firstPartIntegerPartNextLastIndex))
                + parseFloat(
                join(
                        POINT,
                        integerPartAsString.substring(integerPartAsString.length() - 2),
                        Integer.toString(fractionalPart).substring(1)
                )
        ) / 60;
        return integerPart >= 0 ? abs : -1 * abs;
    }
}
