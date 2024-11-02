package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

public abstract class WialonPackageEncoder<PACKAGE extends WialonPackage> extends PackageEncoder<PACKAGE> {

    public WialonPackageEncoder(final Class<PACKAGE> encodedPackageType) {
        super(encodedPackageType);
    }

    @Override
    protected final byte[] encodeInternal(final PACKAGE response) {
        throw new UnsupportedOperationException();
//        return response.getPrefix() + encodeMessage(response) + WialonPackage.POSTFIX;
    }

    protected abstract String encodeMessage(final PACKAGE response);
}
