package by.vladzuev.locationreceiver.protocol.apel.decoder.location;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelCurrentLocationResponsePackageDecoder extends ApelLocationPackageDecoder {
    private static final Integer PREFIX = 101;

    public ApelCurrentLocationResponsePackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected short readSpeed(final ByteBuf buffer) {
        return buffer.readShortLE();
    }

    @Override
    protected Byte readHdop(final ByteBuf buffer) {
        return null;
    }

    @Override
    protected Byte readSatelliteCount(final ByteBuf buffer) {
        return null;
    }

    @Override
    protected double[] readAnalogInputs(final ByteBuf buffer) {
        return new double[0];
    }

    @Override
    protected ApelCurrentLocationResponsePackage createPackage(final ApelLocation location) {
        return new ApelCurrentLocationResponsePackage(location);
    }
}
