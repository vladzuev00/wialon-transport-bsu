package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;

@Component
public final class TeltonikaLocationDecoder {

    public TeltonikaLocation decode(final ByteBuf buffer) {
        final LocalDateTime dateTime = readDateTime(buffer);
        skipPriority(buffer);
        final double longitude = buffer.readInt() / 10000000.0;
        final double latitude = buffer.readInt() / 10000000.0;
        final short altitude = buffer.readShort();
        final short angle = buffer.readShort();
        final byte satelliteCount = buffer.readByte();
        final short speed = buffer.readShort();
        //TODO io
        return new TeltonikaLocation(dateTime, latitude, longitude, altitude, angle, satelliteCount, speed);
    }

    private LocalDateTime readDateTime(final ByteBuf buffer) {
        final long epochSecond = buffer.readLong();
        return ofEpochSecond(epochSecond, 0, UTC);
    }

    private void skipPriority(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }
}
