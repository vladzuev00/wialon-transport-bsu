package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public interface PackageDecoder<PACKAGE extends Package> {
    boolean isAbleToDecode(final ByteBuf buffer);

    PACKAGE decode(final ByteBuf buffer);
}
