package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.location.response.WialonResponseBulkLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseBulkLocationPackageEncoder extends WialonPackageEncoder<WialonResponseBulkLocationPackage> {

    public WialonResponseBulkLocationPackageEncoder() {
        super(WialonResponseBulkLocationPackage.class);
    }

    @Override
    protected String getMessage(final WialonResponseBulkLocationPackage response) {
        return Integer.toString(response.getFixedLocationCount());
    }
}
