package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public abstract class FixPrefixedBinaryPackageDecoder<PREFIX> extends FixPrefixedPackageDecoder<ByteBuf, PREFIX> {

    public FixPrefixedBinaryPackageDecoder(final PREFIX prefix) {
        super(prefix);
    }

    @Override
    public final Package decode(final ByteBuf buffer) {
        buffer.skipBytes(getPrefixByteCount());
        return decodeAfterSkipPrefix(buffer);
    }

    @Override
    protected final PREFIX readPrefix(final ByteBuf buffer) {
        final ByteBuf prefixBytes = getPrefixBytes(buffer);
        try {
            return createPrefix(prefixBytes);
        } finally {
            prefixBytes.release();
        }
    }

    protected abstract int getPrefixByteCount();

    protected abstract Package decodeAfterSkipPrefix(final ByteBuf buffer);

    protected abstract PREFIX createPrefix(final ByteBuf prefixBytes);

    private ByteBuf getPrefixBytes(final ByteBuf buffer) {
        final byte[] bytes = new byte[getPrefixByteCount()];
        buffer.getBytes(0, bytes);
        return wrappedBuffer(bytes);
    }
}
