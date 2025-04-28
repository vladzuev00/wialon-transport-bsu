package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil.*;

@Component
public final class NewWingLocationDecoder {

    public NewWingLocation decode(final ByteBuf buffer) {
        final LocalTime time = decodeTime(buffer);
        final double latitude = decodeLatitude(buffer);
        final double longitude = decodeLongitude(buffer);
        final double hdop = decodeHdop(buffer);
        final short course = decodeCourse(buffer);
        final double speed = decodeSpeed(buffer);
        final LocalDate date = decodeDate(buffer);
        final double[] analogInputs = decodeAnalogInputs(buffer);
        skipFlagByte(buffer);
        skipDiscreteInputStateByte(buffer);
        skipChecksum(buffer);
        return new NewWingLocation(date, time, latitude, longitude, course, speed, hdop, analogInputs);
    }
}
