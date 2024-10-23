package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

public abstract class PrefixedBinaryPackageDecoder<PREFIX> extends PrefixedPackageDecoder<ByteBuf, PREFIX> {

    public PrefixedBinaryPackageDecoder(final PREFIX prefix) {
        super(prefix);
    }

    @Override
    protected final ByteBuf skip(final ByteBuf buffer, final int length) {
        return buffer.skipBytes(length);
    }
}
