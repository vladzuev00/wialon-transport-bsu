package by.vladzuev.locationreceiver.protocol.teltonika.encoder.login;

import by.vladzuev.locationreceiver.protocol.core.encoder.packages.PackageEncoder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseLoginPackage;

public abstract class TeltonikaLoginPackageEncoder<PACKAGE extends TeltonikaResponseLoginPackage> extends PackageEncoder<PACKAGE> {

    public TeltonikaLoginPackageEncoder(final Class<PACKAGE> responseType) {
        super(responseType);
    }

    @Override
    protected final byte[] encodeInternal(final PACKAGE response) {
        return new byte[]{response.getValue()};
    }
}
