package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.ApelLogRecordsRequestPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelLogRecordsRequestPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 131;

    public ApelLogRecordsRequestPackageDecoder() {
        super(PREFIX);
    }

    //TODO
    @Override
    protected ApelLogRecordsRequestPackage decodeStartingFromBody(final ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }
}
