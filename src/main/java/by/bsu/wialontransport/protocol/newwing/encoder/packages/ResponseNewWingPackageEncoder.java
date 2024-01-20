package by.bsu.wialontransport.protocol.newwing.encoder.packages;

import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.ResponseNewWingPackage;

public abstract class ResponseNewWingPackageEncoder<PACKAGE extends ResponseNewWingPackage>
        extends PackageEncoder<PACKAGE> {

    public ResponseNewWingPackageEncoder(final Class<PACKAGE> encodedPackageType) {
        super(encodedPackageType);
    }

    @Override
    protected final String encodeInternal(final PACKAGE response) {
        return response.getValue();
    }
}
