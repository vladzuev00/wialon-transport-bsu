package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonResponsePingPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponsePingPackageEncoder extends WialonPackageEncoder<WialonResponsePingPackage> {
    private static final String ENCODED_MESSAGE = "";

    public WialonResponsePingPackageEncoder() {
        super(WialonResponsePingPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponsePingPackage response) {
        return ENCODED_MESSAGE;
    }
}
