package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

public abstract class FixPrefixedBinaryPackageDecoder<PREFIX> extends FixPrefixedPackageDecoder<ByteBuf, PREFIX> {

    public FixPrefixedBinaryPackageDecoder(final PREFIX prefix) {
        super(prefix);
    }

    @Override
    protected final PREFIX readPrefix(final ByteBuf buffer) {
        final ByteBuf bytes = buffer.slice(0, getPrefixByteCount());
        bytes.retain();
        try {
            return createPrefix(bytes);
        } finally {
            bytes.release();
        }
    }

    @Override
    protected final ByteBuf removePrefix(final ByteBuf buffer) {
        return buffer.skipBytes(getPrefixByteCount());
    }

    protected abstract int getPrefixByteCount();

    protected abstract PREFIX createPrefix(final ByteBuf prefixBytes);
}
