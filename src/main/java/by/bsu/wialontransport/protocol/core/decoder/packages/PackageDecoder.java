package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public interface PackageDecoder {
    boolean isAbleToDecode(final ByteBuf buffer);

    Package decode(final ByteBuf buffer);
}
