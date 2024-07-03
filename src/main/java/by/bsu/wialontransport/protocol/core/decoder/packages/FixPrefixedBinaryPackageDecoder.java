package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public abstract class FixPrefixedBinaryPackageDecoder<PREFIX> extends FixPrefixedPackageDecoder<ByteBuf, PREFIX> {

    public FixPrefixedBinaryPackageDecoder(final PREFIX prefix) {
        super(prefix);
    }

    @Override
    public final Package decode(final ByteBuf buffer) {
        buffer.skipBytes(getPrefixByteCount());
        return decodeAfterSkipPrefix(buffer);
    }

    protected abstract int getPrefixByteCount();

    protected abstract Package decodeAfterSkipPrefix(final ByteBuf buffer);
}
