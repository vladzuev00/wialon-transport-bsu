package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;

public interface PackageDecoder<PREFIX, SOURCE> {
    boolean isAbleToDecode(final PREFIX prefix);

    Package decode(final SOURCE source);
}
