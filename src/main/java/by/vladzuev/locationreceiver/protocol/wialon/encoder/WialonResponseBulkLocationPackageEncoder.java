package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseBulkLocationPackageEncoder extends WialonPackageEncoder<WialonResponseBulkLocationPackage> {

    public WialonResponseBulkLocationPackageEncoder() {
        super(WialonResponseBulkLocationPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponseBulkLocationPackage response) {
        return Integer.toString(response.getFixedLocationCount());
    }
}
