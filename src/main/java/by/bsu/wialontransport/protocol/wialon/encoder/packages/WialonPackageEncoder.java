package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public abstract class WialonPackageEncoder<PACKAGE extends WialonPackage> extends PackageEncoder<PACKAGE> {

    public WialonPackageEncoder(final Class<PACKAGE> encodedPackageType) {
        super(encodedPackageType);
    }

    @Override
    protected final String encodeInternal(final PACKAGE response) {
        return response.getPrefix() + encodeMessage(response) + response.getPostfix();
    }

    protected abstract String encodeMessage(final PACKAGE response);
}
