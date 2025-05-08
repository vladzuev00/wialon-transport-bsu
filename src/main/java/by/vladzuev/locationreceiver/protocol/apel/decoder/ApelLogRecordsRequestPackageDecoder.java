package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import by.vladzuev.locationreceiver.protocol.apel.model.ApelLogRecordsRequestPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelLogRecordsRequestPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 131;

    public ApelLogRecordsRequestPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelLogRecordsRequestPackage decodeStartingFromBody(final ByteBuf buffer) {
        throw new RuntimeException();
//        final int locationCount = buffer.readUnsignedShortLE();
//        return range(0, locationCount)
//                .mapToObj(i -> readLocation(buffer))
//                .collect(collectingAndThen(toList(), ApelLogRecordsRequestPackage::new));
    }

    private ApelLocation readLocation(final ByteBuf buffer) {
        buffer.skipBytes(Integer.BYTES);
        throw new RuntimeException();
    }
}
