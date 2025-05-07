package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelRequestCurrentLocationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelRequestCurrentLocationPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 100;

    public ApelRequestCurrentLocationPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelRequestCurrentLocationPackage decodeStartingFromBody(final ByteBuf buffer) {
        final int epochSeconds = buffer.readIntLE();
        final int latitude = buffer.readIntLE();
        final int longitude = buffer.readIntLE();
        final short speed = buffer.readShortLE();
        final short course = buffer.readShortLE();
        final short altitude = buffer.readShortLE();
        return new ApelRequestCurrentLocationPackage(
                new ApelLocation(
                        epochSeconds,
                        latitude,
                        longitude,
                        speed,
                        course,
                        altitude
                )
        );
    }
}
