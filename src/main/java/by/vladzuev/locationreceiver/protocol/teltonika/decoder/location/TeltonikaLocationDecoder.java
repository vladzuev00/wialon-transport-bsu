package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;

@Component
public final class TeltonikaLocationDecoder {
    private static final double GPS_COORDINATE_DELIMITER = 10000000.0;
    private static final int IO_PREFIX_BYTE_COUNT = 2;
    private static final int IO_N1_VALUE_BYTE_COUNT = 1;
    private static final int IO_N2_VALUE_BYTE_COUNT = 2;
    private static final int IO_N4_VALUE_BYTE_COUNT = 4;
    private static final int IO_N8_VALUE_BYTE_COUNT = 8;

    public TeltonikaLocation decode(final ByteBuf buffer) {
        final LocalDateTime dateTime = readDateTime(buffer);
        skipPriority(buffer);
        final double longitude = readGpsCoordinate(buffer);
        final double latitude = readGpsCoordinate(buffer);
        final short altitude = buffer.readShort();
        final short angle = buffer.readShort();
        final byte satelliteCount = buffer.readByte();
        final short speed = buffer.readShort();
        skipAllIO(buffer);
        return new TeltonikaLocation(dateTime, latitude, longitude, altitude, angle, satelliteCount, speed);
    }

    private LocalDateTime readDateTime(final ByteBuf buffer) {
        final long epochSecond = buffer.readLong();
        return ofEpochSecond(epochSecond, 0, UTC);
    }

    private double readGpsCoordinate(final ByteBuf buffer) {
        return buffer.readInt() / GPS_COORDINATE_DELIMITER;
    }

    private void skipPriority(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipAllIO(final ByteBuf buffer) {
        buffer.skipBytes(IO_PREFIX_BYTE_COUNT);
        skipIO(buffer, IO_N1_VALUE_BYTE_COUNT);
        skipIO(buffer, IO_N2_VALUE_BYTE_COUNT);
        skipIO(buffer, IO_N4_VALUE_BYTE_COUNT);
        skipIO(buffer, IO_N8_VALUE_BYTE_COUNT);
    }

    private void skipIO(final ByteBuf buffer, final int valueByteCount) {
        final int count = buffer.readByte();
        buffer.skipBytes(count);
        buffer.skipBytes(count * valueByteCount);
    }
}
