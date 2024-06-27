package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public abstract class PrefixiedPackageDecoder<PACKAGE extends Package, PREFIX> implements PackageDecoder<PACKAGE> {

    @Override
    public final boolean isAbleToDecode(final ByteBuf buffer) {
        final PREFIX prefix = readPrefix(buffer);
        return isAbleToDecode(prefix);
    }

    protected abstract PREFIX readPrefix(final ByteBuf buffer);

    protected abstract boolean isAbleToDecode(final PREFIX prefix);
}
