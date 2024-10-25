package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator.NewWingLatitudeCalculator;
import by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator.NewWingLongitudeCalculator;
import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.util.NumberUtil.concatToDouble;

@Component
@RequiredArgsConstructor
public final class NewWingDataDecoder {
    private static final int YEAR_MARK_POINT = 2000;

    private final NewWingLatitudeCalculator latitudeCalculator;
    private final NewWingLongitudeCalculator longitudeCalculator;

    public NewWingLocation decodeNext(final ByteBuf buffer) {
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
        return new NewWingLocation(
                createDateTime(year, month, day, hour, minute, second),
                latitudeCalculator.calculate(latitudeIntegerPart, latitudeFractionalPart),
                longitudeCalculator.calculate(longitudeIntegerPart, longitudeFractionalPart),
                course,
                concatToDouble(speedIntegerPart, speedFractionalPart),
                concatToDouble(hdopIntegerPart, hdopFractionalPart),
                new double[]{
                        firstAnalogInputLevel,
                        secondAnalogInputLevel,
                        thirdAnalogInputLevel,
                        fourthAnalogInputLevel
                }
        );
    }

    private short decodeShort(final ByteBuf buffer) {
        return buffer.readShortLE();
    }

    private byte decodeByte(final ByteBuf buffer) {
        return buffer.readByte();
    }

    private LocalDateTime createDateTime(final byte year,
                                         final byte month,
                                         final byte day,
                                         final byte hour,
                                         final byte minute,
                                         final byte second) {
        return LocalDateTime.of(YEAR_MARK_POINT + year, month, day, hour, minute, second);
    }

    private Coordinate calculateCoordinate(final short latitudeIntegerPart,
                                           final short latitudeFractionalPart,
                                           final short longitudeIntegerPart,
                                           final short longitudeFractionalPart) {
        final double latitude = latitudeCalculator.calculate(latitudeIntegerPart, latitudeFractionalPart);
        final double longitude = longitudeCalculator.calculate(longitudeIntegerPart, longitudeFractionalPart);
        return new Coordinate(latitude, longitude);
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
