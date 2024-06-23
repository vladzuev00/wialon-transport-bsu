package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

public abstract class PrefixiedPackageDecoder<PREFIX> implements PackageDecoder {

    @Override
    public final boolean isAbleToDecode(final ByteBuf buffer) {
        final PREFIX prefix = getPrefix(buffer);
        return isAbleToDecode(prefix);
    }

    protected abstract PREFIX getPrefix(final ByteBuf buffer);

    protected abstract boolean isAbleToDecode(final PREFIX prefix);
}
