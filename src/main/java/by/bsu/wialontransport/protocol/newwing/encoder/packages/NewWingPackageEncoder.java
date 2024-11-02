package by.bsu.wialontransport.protocol.newwing.encoder.packages;

import by.bsu.wialontransport.protocol.core.encoder.packages.StringPackageEncoder;
import by.bsu.wialontransport.protocol.newwing.model.response.NewWingResponsePackage;

public abstract class NewWingPackageEncoder<PACKAGE extends NewWingResponsePackage> extends StringPackageEncoder<PACKAGE> {

    public NewWingPackageEncoder(final Class<PACKAGE> responseType) {
        super(responseType);
    }

    @Override
    protected final String getString(final PACKAGE response) {
        return response.getValue();
    }
}
