package by.bsu.wialontransport.protocol.core.decoder.packages.binary;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;

public abstract class PrefixedPackageDecoder<PREFIX> implements PackageDecoder {

    @Override
    public final boolean isAbleToDecode(final ByteBuf buffer) {
        final PREFIX prefix = readPrefix(buffer);
        return isAbleToDecode(prefix);
    }

    protected abstract PREFIX readPrefix(final ByteBuf buffer);

    protected abstract boolean isAbleToDecode(final PREFIX prefix);
}
