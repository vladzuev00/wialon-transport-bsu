package by.vladzuev.locationreceiver.protocol.teltonika.encoder;

import by.vladzuev.locationreceiver.protocol.core.encoder.packages.PackageEncoder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaResponseLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class TeltonikaLocationPackageEncoder extends PackageEncoder<TeltonikaResponseLocationPackage> {

    public TeltonikaLocationPackageEncoder() {
        super(TeltonikaResponseLocationPackage.class);
    }

    @Override
    protected byte[] encodeInternal(final TeltonikaResponseLocationPackage response) {
        return new byte[]{(byte) response.getLocationCount()};
    }
}
