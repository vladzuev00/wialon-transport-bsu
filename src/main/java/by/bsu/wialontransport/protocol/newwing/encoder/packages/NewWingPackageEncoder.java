package by.bsu.wialontransport.protocol.newwing.encoder.packages;

import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.newwing.model.response.NewWingResponsePackage;

public abstract class NewWingPackageEncoder<PACKAGE extends NewWingResponsePackage> extends PackageEncoder<PACKAGE> {

    public NewWingPackageEncoder(final Class<PACKAGE> encodedPackageType) {
        super(encodedPackageType);
    }

    @Override
    protected final byte[] encodeInternal(final PACKAGE response) {
        throw new UnsupportedOperationException();
//        return response.getValue();
    }
}
