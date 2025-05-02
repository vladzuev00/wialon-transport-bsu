package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;

public abstract class TeltonikaPackageDecoder implements PackageDecoder<ByteBuf> {

    @Override
    public final Object decode(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
        return decodeInternal(buffer);
    }

    protected abstract Object decodeInternal(final ByteBuf buffer);
}
