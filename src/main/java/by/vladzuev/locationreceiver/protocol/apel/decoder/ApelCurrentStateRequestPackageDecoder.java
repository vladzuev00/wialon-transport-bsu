package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentStateRequestPackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelCurrentStateRequestPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 92;

    public ApelCurrentStateRequestPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelCurrentStateRequestPackage decodeStartingFromBody(final ByteBuf buffer) {
        final int epochSeconds = buffer.readIntLE();
        final int latitude = buffer.readIntLE();
        final int longitude = buffer.readIntLE();
        final byte speed = buffer.readByte();
        buffer.skipBytes(Byte.BYTES);  //hdop
        final short course = buffer.readShortLE();
        final short altitude = buffer.readShortLE();
        final byte satelliteCount = buffer.readByte();
        buffer.skipBytes(Byte.BYTES);   //GSM
        buffer.skipBytes(Short.BYTES);   //eventType
        buffer.skipBytes(Integer.BYTES);   //metersTraveled
        buffer.skipBytes(Byte.BYTES);   //DI
        buffer.skipBytes(Byte.BYTES);   //DO
        //TODO analogInputs
        final short firstAnalogInput = buffer.readShortLE();
        final short secondAnalogInput = buffer.readShortLE();
        final short thirdAnalogInput = buffer.readShortLE();
        final short fourthAnalogInput = buffer.readShortLE();
        final short fifthAnalogInput = buffer.readShortLE();
        final short sixthAnalogInput = buffer.readShortLE();
        final short seventhAnalogInput = buffer.readShortLE();
        final short eighthAnalogInput = buffer.readShortLE();
        return new ApelCurrentStateRequestPackage(
                new ApelLocation(
                        epochSeconds,
                        latitude,
                        longitude,
                        speed,
                        course,
                        altitude,
                        satelliteCount,
                        new double[]{
                                firstAnalogInput,
                                secondAnalogInput,
                                thirdAnalogInput,
                                fourthAnalogInput,
                                fifthAnalogInput,
                                sixthAnalogInput,
                                seventhAnalogInput,
                                eighthAnalogInput
                        }
                )
        );
    }
}
