package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.ApelLogRecordsRequestPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
public final class ApelLogRecordsRequestPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 131;

    public ApelLogRecordsRequestPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelLogRecordsRequestPackage decodeStartingFromBody(final ByteBuf buffer) {
//        final int locationCount = buffer.readUnsignedShortLE();
//        return range(0, locationCount)
//                .mapToObj(i -> readLocation(buffer))
//                .collect(collectingAndThen(toList(), ApelLogRecordsRequestPackage::new));
    }
}
