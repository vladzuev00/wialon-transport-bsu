package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.protocolpackage.Package;

public abstract class PackageStringDecoder<PREFIX, PACKAGE extends Package>
        extends PackageDecoder<PREFIX, String, PACKAGE> {

    public PackageStringDecoder(final PREFIX packagePrefix) {
        super(packagePrefix);
    }

}
