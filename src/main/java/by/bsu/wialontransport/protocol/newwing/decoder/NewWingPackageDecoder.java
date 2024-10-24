package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;

public abstract class NewWingPackageDecoder extends PrefixedByStringBinaryPackageDecoder {

    public NewWingPackageDecoder(final String requiredPrefix) {
        super(requiredPrefix);
    }
}
