package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public interface PackageDecoder<PREFIX> {
    boolean isAbleToDecode(final PREFIX prefix);

    Package decode(final ByteBuf buffer);
}
