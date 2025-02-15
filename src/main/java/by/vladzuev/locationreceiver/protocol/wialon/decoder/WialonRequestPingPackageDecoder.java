package by.vladzuev.locationreceiver.protocol.wialon.decoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestPingPackageDecoder extends WialonPackageDecoder {

    public WialonRequestPingPackageDecoder() {
        super(WialonRequestPingPackage.PREFIX);
    }

    @Override
    protected WialonRequestPingPackage decodeMessage(final String message) {
        return new WialonRequestPingPackage();
    }
}
