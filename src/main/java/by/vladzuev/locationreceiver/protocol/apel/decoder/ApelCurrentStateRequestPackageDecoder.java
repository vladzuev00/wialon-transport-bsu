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
        final byte hdop = buffer.readByte();
        final short course = buffer.readShortLE();
        final short altitude = buffer.readShortLE();
        final byte satelliteCount = buffer.readByte();
        skipGSM(buffer);
        skipEventType(buffer);
        skipMetersTraveled(buffer);
        skipDI(buffer);
        skipDO(buffer);
        final double[] analogInputs = readAnalogInputs(buffer);
        return createPackage(epochSeconds, latitude, longitude, speed, hdop, course, altitude, satelliteCount, analogInputs);
    }

    private void skipGSM(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipEventType(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipMetersTraveled(final ByteBuf buffer) {
        buffer.skipBytes(Integer.BYTES);
    }

    private void skipDI(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipDO(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private double[] readAnalogInputs(final ByteBuf buffer) {
        return new double[]{
                buffer.readShortLE(),
                buffer.readShortLE(),
                buffer.readShortLE(),
                buffer.readShortLE(),
                buffer.readShortLE(),
                buffer.readShortLE(),
                buffer.readShortLE(),
                buffer.readShortLE()
        };
    }

    private ApelCurrentStateRequestPackage createPackage(final int epochSeconds,
                                                         final int latitude,
                                                         final int longitude,
                                                         final byte speed,
                                                         final byte hdop,
                                                         final short course,
                                                         final short altitude,
                                                         final byte satelliteCount,
                                                         final double[] analogInputs) {
        return new ApelCurrentStateRequestPackage(
                new ApelLocation(
                        epochSeconds,
                        latitude,
                        longitude,
                        speed,
                        (double) hdop,
                        course,
                        altitude,
                        satelliteCount,
                        analogInputs
                )
        );
    }
}
