package by.vladzuev.locationreceiver.protocol.apel.decoder.location;

import by.vladzuev.locationreceiver.protocol.apel.decoder.ApelPackageDecoder;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;

public abstract class ApelLocationPackageDecoder extends ApelPackageDecoder {

    public ApelLocationPackageDecoder(final Integer prefix) {
        super(prefix);
    }

    @Override
    protected final Object decodeStartingFromBody(final ByteBuf buffer) {
        final ApelLocation location = readLocation(buffer);
        return createPackage(location);
    }

    protected abstract short readSpeed(final ByteBuf buffer);

    protected abstract Byte readHdop(final ByteBuf buffer);

    protected abstract Byte readSatelliteCount(final ByteBuf buffer);

    protected abstract double[] readAnalogInputs(final ByteBuf buffer);

    protected abstract Object createPackage(final ApelLocation location);

    private ApelLocation readLocation(final ByteBuf buffer) {
        final int epochSeconds = buffer.readIntLE();
        final int latitude = buffer.readIntLE();
        final int longitude = buffer.readIntLE();
        final short speed = readSpeed(buffer);
        final Byte hdop = readHdop(buffer);
        final short course = buffer.readShortLE();
        final short altitude = buffer.readShortLE();
        final Byte satelliteCount = readSatelliteCount(buffer);
        final double[] analogInputs = readAnalogInputs(buffer);
        return new ApelLocation(epochSeconds, latitude, longitude, speed, hdop, course, altitude, satelliteCount, analogInputs);
    }
}
