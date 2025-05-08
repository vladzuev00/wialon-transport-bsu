package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelCurrentLocationResponsePackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 101;

    public ApelCurrentLocationResponsePackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelCurrentLocationResponsePackage decodeStartingFromBody(final ByteBuf buffer) {
        final int epochSeconds = buffer.readIntLE();
        final int latitude = buffer.readIntLE();
        final int longitude = buffer.readIntLE();
        final short speed = buffer.readShortLE();
        final short course = buffer.readShortLE();
        final short altitude = buffer.readShortLE();
        return createPackage(epochSeconds, latitude, longitude, speed, course, altitude);
    }

    private ApelCurrentLocationResponsePackage createPackage(final int epochSeconds,
                                                             final int latitude,
                                                             final int longitude,
                                                             final short speed,
                                                             final short course,
                                                             final short altitude) {
        return new ApelCurrentLocationResponsePackage(
                ApelLocation.builder()
                        .epochSeconds(epochSeconds)
                        .latitude(latitude)
                        .longitude(longitude)
                        .speed(speed)
                        .course(course)
                        .altitude(altitude)
                        .build()
        );
    }
}
