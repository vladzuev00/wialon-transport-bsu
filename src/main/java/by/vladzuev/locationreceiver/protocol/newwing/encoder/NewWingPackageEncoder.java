package by.vladzuev.locationreceiver.protocol.newwing.encoder;

import by.vladzuev.locationreceiver.protocol.core.encoder.packages.TextPackageEncoder;
import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingResponsePackage;

public abstract class NewWingPackageEncoder<PACKAGE extends NewWingResponsePackage> extends TextPackageEncoder<PACKAGE> {

    public NewWingPackageEncoder(final Class<PACKAGE> responseType) {
        super(responseType);
    }

    @Override
    protected final String getString(final PACKAGE response) {
        return response.getValue();
    }
}
