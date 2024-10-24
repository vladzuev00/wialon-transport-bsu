package by.bsu.wialontransport.protocol.jt808.util;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

@UtilityClass
public final class JT808Util {
    private static final int PHONE_NUMBER_BYTE_COUNT = 6;
    private static final int MANUFACTURER_ID_BYTE_COUNT = 5;
    private static final double LATITUDE_DELIMITER = 1000000.;
    private static final double LONGITUDE_DELIMITER = 10000000.;
    private static final int DATE_TIME_BYTE_COUNT = 6;
    private static final DateTimeFormatter DATE_FORMAT = ofPattern("yyMMddHHmmss");

    public static String decodePhoneNumber(final ByteBuf buffer) {
        return decodeBcdString(buffer, PHONE_NUMBER_BYTE_COUNT);
    }

    public static String decodeManufacturerId(final ByteBuf buffer) {
        return buffer.readCharSequence(MANUFACTURER_ID_BYTE_COUNT, US_ASCII)
                .toString()
                .trim();
    }

    public static double decodeLatitude(final ByteBuf buffer) {
        return buffer.readUnsignedInt() / LATITUDE_DELIMITER;
    }

    public static double decodeLongitude(final ByteBuf buffer) {
        return buffer.readUnsignedInt() / LONGITUDE_DELIMITER;
    }

    public static LocalDateTime decodeDateTime(final ByteBuf buffer) {
        final String content = decodeBcdString(buffer, DATE_TIME_BYTE_COUNT);
        return parse(content, DATE_FORMAT);
    }

    private static String decodeBcdString(final ByteBuf buffer, final int length) {
        final byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return range(0, length)
                .mapToObj(i -> decodeBcdString(bytes[i]))
                .collect(joining());
    }

    private String decodeBcdString(final byte source) {
        return Integer.toString((source & 0xf0) >>> 4) + (source & 0x0f);
    }
}
