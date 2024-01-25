package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseBlackBoxPackageEncoder extends WialonPackageEncoder<WialonResponseBlackBoxPackage> {

    public WialonResponseBlackBoxPackageEncoder() {
        super(WialonResponseBlackBoxPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponseBlackBoxPackage response) {
        return Integer.toString(response.getAmountFixedMessages());
    }
}
