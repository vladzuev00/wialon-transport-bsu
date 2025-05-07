package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PrefixedByUnsignedShortLEBinaryPackageDecoder;
import io.netty.buffer.ByteBuf;

public abstract class ApelPackageDecoder extends PrefixedByUnsignedShortLEBinaryPackageDecoder {

    public ApelPackageDecoder(final Integer prefix) {
        super(prefix);
    }

    @Override
    protected final Object decodeInternal(final ByteBuf buffer) {
        skipBodyLength(buffer);
        return decodeStartingFromBody(buffer);
    }

    protected abstract Object decodeStartingFromBody(final ByteBuf buffer);

    private void skipBodyLength(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }
}
