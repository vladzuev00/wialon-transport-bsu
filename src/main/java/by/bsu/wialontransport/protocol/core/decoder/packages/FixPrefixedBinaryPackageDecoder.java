package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public abstract class FixPrefixedBinaryPackageDecoder<PREFIX> extends FixPrefixedPackageDecoder<ByteBuf, PREFIX> {

    public FixPrefixedBinaryPackageDecoder(final PREFIX prefix) {
        super(prefix);
    }

    @Override
    public final Package decode(final ByteBuf buffer) {
        skipPrefix(buffer);
        return decodeAfterSkipPrefix(buffer);
    }

    @Override
    protected final PREFIX readPrefix(final ByteBuf buffer) {
        final ByteBuf bytes = getPrefixBytes(buffer);
        try {
            return createPrefix(bytes);
        } finally {
            bytes.release();
        }
    }

    protected abstract int getPrefixByteCount();

    protected abstract Package decodeAfterSkipPrefix(final ByteBuf buffer);

    protected abstract PREFIX createPrefix(final ByteBuf prefixBytes);

    private void skipPrefix(final ByteBuf buffer) {
        buffer.skipBytes(getPrefixByteCount());
    }

    private ByteBuf getPrefixBytes(final ByteBuf buffer) {
        return buffer.slice(0, getPrefixByteCount());
    }
}
