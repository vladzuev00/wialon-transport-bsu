package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;

public abstract class ApelLocationDecoder {

    public ApelLocation decode(final ByteBuf buffer) {
        throw new RuntimeException();
    }
}
