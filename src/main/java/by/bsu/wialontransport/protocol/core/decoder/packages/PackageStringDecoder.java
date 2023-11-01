package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.protocolpackage.Package;

public abstract class PackageStringDecoder<PACKAGE extends Package> extends PackageDecoder<String, String, PACKAGE> {

    public PackageStringDecoder(final String packagePrefix) {
        super(packagePrefix);
    }

}
