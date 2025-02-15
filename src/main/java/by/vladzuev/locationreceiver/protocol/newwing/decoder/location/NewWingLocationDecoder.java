package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.newwing.model.NewWingLocation;
import by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public final class NewWingLocationDecoder {

    public NewWingLocation decode(final ByteBuf buffer) {
        final LocalTime time = NewWingUtil.decodeTime(buffer);
        final double latitude = NewWingUtil.decodeLatitude(buffer);
        final double longitude = NewWingUtil.decodeLongitude(buffer);
        final double hdop = NewWingUtil.decodeHdop(buffer);
        final short course = NewWingUtil.decodeShort(buffer);
        final double speed = NewWingUtil.decodeSpeed(buffer);
        final LocalDate date = NewWingUtil.decodeDate(buffer);
        final double[] analogInputs = NewWingUtil.decodeAnalogInputs(buffer);
        skipFlagByte(buffer);
        skipDiscreteInputStateByte(buffer);
        skipChecksum(buffer);
        return new NewWingLocation(date, time, latitude, longitude, course, speed, hdop, analogInputs);
    }

    private void skipFlagByte(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipDiscreteInputStateByte(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipChecksum(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }
}
