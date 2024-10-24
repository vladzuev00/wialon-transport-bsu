package by.bsu.wialontransport.protocol.jt808.util;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.time.format.DateTimeFormatter.ofPattern;

@UtilityClass
public final class JT808Util {
    private static final int PHONE_NUMBER_BYTE_COUNT = 6;
    private static final int MANUFACTURER_ID_BYTE_COUNT = 5;
    private static final DateTimeFormatter DATE_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String decodePhoneNumber(final ByteBuf buffer) {
        final byte[] bytes = new byte[PHONE_NUMBER_BYTE_COUNT];
        buffer.readBytes(bytes);
        return decodeBcdString(bytes);
    }

    public static String decodeManufacturerId(final ByteBuf buffer) {
        return buffer.readCharSequence(MANUFACTURER_ID_BYTE_COUNT, US_ASCII)
                .toString()
                .trim();
    }

    public static LocalDateTime decodeDateTime(final ByteBuf buffer) {
        byte[] bytes = new byte[6];
        buffer.readBytes(bytes);
        //TODO: use decodeBcdString
        return LocalDateTime.parse(toBcdTimeString(bytes), DATE_FORMAT);
    }

    public static String decodeBcdString(byte[] bytes) {
        StringBuilder temp = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            temp.append((aByte & 0xf0) >>> 4);
            temp.append(aByte & 0x0f);
        }
        return temp.toString();
    }

    public static double decodeLatitude(ByteBuf buffer) {
        return buffer.readUnsignedInt() / 1000000.;
    }

    public static double decodeLongitude(ByteBuf buffer) {
        return buffer.readUnsignedInt() / 10000000.;
    }

    public static String toBcdTimeString(byte[] bs) {
        if (bs.length != 6 && bs.length != 7) {
            return "0000-00-00 00:00:00";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (bs.length == 6) {
            sb.append("20");
        } else {
            sb.append(BCDtoString(bs[i++]));
        }
        sb.append(BCDtoString(bs[i++]));
        sb.append("-").append(BCDtoString(bs[i++]));
        sb.append("-").append(BCDtoString(bs[i++]));
        sb.append(" ").append(BCDtoString(bs[i++]));
        sb.append(":").append(BCDtoString(bs[i++]));
        sb.append(":").append(BCDtoString(bs[i]));
        return sb.toString();
    }

    public static String BCDtoString(byte bcd) {
        StringBuilder sb = new StringBuilder();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }
}
