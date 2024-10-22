package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;

public interface PackageDecoder<SOURCE> {
    boolean isAbleToDecode(final SOURCE source);

    Object decode(final SOURCE source);
}
