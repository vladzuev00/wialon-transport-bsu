package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonResponsePingPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponsePingPackageEncoder extends WialonPackageEncoder<WialonResponsePingPackage> {

    public WialonResponsePingPackageEncoder() {
        super(WialonResponsePingPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponsePingPackage response) {
        return "";
    }
}
