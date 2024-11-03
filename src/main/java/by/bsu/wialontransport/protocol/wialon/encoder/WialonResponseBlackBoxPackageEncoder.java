package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseBlackBoxPackageEncoder extends WialonPackageEncoder<WialonResponseBulkLocationPackage> {

    public WialonResponseBlackBoxPackageEncoder() {
        super(WialonResponseBulkLocationPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponseBulkLocationPackage response) {
        return Integer.toString(response.getFixedMessageCount());
    }
}
