package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseSingleLocationPackageEncoder extends WialonPackageEncoder<WialonResponseSingleLocationPackage> {

    public WialonResponseSingleLocationPackageEncoder() {
        super(WialonResponseSingleLocationPackage.class);
    }

    @Override
    protected String getMessage(final WialonResponseSingleLocationPackage response) {
        return Byte.toString(response.getStatus().getValue());
    }
}
