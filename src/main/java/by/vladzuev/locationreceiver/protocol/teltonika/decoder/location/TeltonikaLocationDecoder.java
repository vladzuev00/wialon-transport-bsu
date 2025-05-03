package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class TeltonikaLocationDecoder {

    public TeltonikaLocation decode(final ByteBuf buffer) {
        final float longitude = buffer.readFloat();
        final float latitude = buffer.readFloat();
        final short altitude = buffer.readShort();
        final short angle = buffer.readShort();
        final byte satelliteCount = buffer.readByte();
        final short speed = buffer.readShort();
        return new TeltonikaLocation(longitude, latitude, altitude, angle, satelliteCount, speed);
    }
}
