package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.model.Coordinate;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.util.NumberUtil.createDoubleByParts;
import static by.bsu.wialontransport.util.coordinate.NewWingCoordinateUtil.calculateLatitude;
import static by.bsu.wialontransport.util.coordinate.NewWingCoordinateUtil.calculateLongitude;

@Component
public final class NewWingDataDecoder {
    private static final int YEAR_MARK_POINT = 2000;

    public Data decodeNext(final ByteBuf buffer) {
        final byte hour = decodeByte(buffer);
        final byte minute = decodeByte(buffer);
        final byte second = decodeByte(buffer);
        final short latitudeIntegerPart = decodeShort(buffer);
        final short latitudeFractionalPart = decodeShort(buffer);
        final short longitudeIntegerPart = decodeShort(buffer);
        final short longitudeFractionalPart = decodeShort(buffer);
        final byte hdopIntegerPart = decodeByte(buffer);
        final byte hdopFractionalPart = decodeByte(buffer);
        final short course = decodeShort(buffer);
        final short speedIntegerPart = decodeShort(buffer);
        final byte speedFractionalPart = decodeByte(buffer);
        final byte day = decodeByte(buffer);
        final byte month = decodeByte(buffer);
        final byte year = decodeByte(buffer);
        final short firstAnalogInputLevel = decodeShort(buffer);
        final short secondAnalogInputLevel = decodeShort(buffer);
        final short thirdAnalogInputLevel = decodeShort(buffer);
        final short fourthAnalogInputLevel = decodeShort(buffer);
        skipFlagByte(buffer);
        skipDiscreteInputStateByte(buffer);
        skipChecksum(buffer);
        return Data.builder()
                .dateTime(LocalDateTime.of(YEAR_MARK_POINT + year, month, day, hour, minute, second))
                .coordinate(new Coordinate(calculateLatitude(latitudeIntegerPart, latitudeFractionalPart), calculateLongitude(longitudeIntegerPart, longitudeFractionalPart)))
                .course(course)
                .speed(createDoubleByParts(speedIntegerPart, speedFractionalPart))
                .hdop(createDoubleByParts(hdopIntegerPart, hdopFractionalPart))
                .analogInputs(new double[]{firstAnalogInputLevel, secondAnalogInputLevel, thirdAnalogInputLevel, fourthAnalogInputLevel})
                .build();
    }

    private short decodeShort(final ByteBuf buffer) {
        return buffer.readShortLE();
    }

    private byte decodeByte(final ByteBuf buffer) {
        return buffer.readByte();
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
