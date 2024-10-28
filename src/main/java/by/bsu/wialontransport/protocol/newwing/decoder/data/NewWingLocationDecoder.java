package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static by.bsu.wialontransport.protocol.newwing.util.NewWingUtil.*;

@Component
public final class NewWingLocationDecoder {

    public NewWingLocation decode(final ByteBuf buffer) {
        final LocalTime time = decodeTime(buffer);
        final double latitude = decodeLatitude(buffer);
        final double longitude = decodeLongitude(buffer);
        final double hdop = decodeHdop(buffer);
        final short course = decodeShort(buffer);
        final double speed = decodeSpeed(buffer);
        final LocalDate date = decodeDate(buffer);
        final LocalDateTime dateTime = LocalDateTime.of(date, time);
        final double[] analogInputs = decodeAnalogInputs(buffer);
        skipFlagByte(buffer);
        skipDiscreteInputStateByte(buffer);
        skipChecksum(buffer);
        return new NewWingLocation(dateTime, latitude, longitude, course, speed, hdop, analogInputs);
    }

    private short decodeShort(final ByteBuf buffer) {
        return buffer.readShortLE();
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
