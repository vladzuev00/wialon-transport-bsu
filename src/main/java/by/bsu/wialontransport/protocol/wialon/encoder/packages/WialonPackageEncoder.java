package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public class WialonPackageEncoder<PACKAGE extends WialonPackage> extends PackageEncoder<PACKAGE> {
    public WialonPackageEncoder(Class<PACKAGE> encodedPackageType) {
        super(encodedPackageType);
    }

    @Override
    protected String encodeInternal(PACKAGE response) {
        throw new UnsupportedOperationException();
    }
}
