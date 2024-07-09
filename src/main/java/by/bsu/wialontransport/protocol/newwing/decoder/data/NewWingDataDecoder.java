package by.bsu.wialontransport.protocol.newwing.decoder.data;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingDataDecoder {

    public NewWingData decodeNext(final ByteBuf buffer) {
        return NewWingData.builder()
                .hour(decodeByte(buffer))
                .minute(decodeByte(buffer))
                .second(decodeByte(buffer))
                .latitudeIntegerPart(decodeShort(buffer))
                .latitudeFractionalPart(decodeShort(buffer))
                .longitudeIntegerPart(decodeShort(buffer))
                .longitudeFractionalPart(decodeShort(buffer))
                .hdopIntegerPart(decodeByte(buffer))
                .hdopFractionalPart(decodeByte(buffer))
                .course(decodeShort(buffer))
                .speedIntegerPart(decodeShort(buffer))
                .speedFractionalPart(decodeByte(buffer))
                .day(decodeByte(buffer))
                .month(decodeByte(buffer))
                .year(decodeByte(buffer))
                .firstAnalogInputLevel(decodeShort(buffer))
                .secondAnalogInputLevel(decodeShort(buffer))
                .thirdAnalogInputLevel(decodeShort(buffer))
                .fourthAnalogInputLevel(decodeShort(buffer))
                .flagByte(decodeByte(buffer))
                .discreteInputStateByte(decodeByte(buffer))
                .checksum(decodeShort(buffer))
                .build();
    }

    private short decodeShort(final ByteBuf buffer) {
        return buffer.readShortLE();
    }

    private byte decodeByte(final ByteBuf buffer) {
        return buffer.readByte();
    }
}