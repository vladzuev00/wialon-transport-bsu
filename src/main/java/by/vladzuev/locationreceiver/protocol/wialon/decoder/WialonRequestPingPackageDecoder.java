package by.vladzuev.locationreceiver.protocol.wialon.decoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.ping.WialonRequestPingPackage;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.protocol.wialon.model.ping.WialonRequestPingPackage.PREFIX;

@Component
public final class WialonRequestPingPackageDecoder extends WialonPackageDecoder {

    public WialonRequestPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestPingPackage decodeMessage(final String message) {
        return new WialonRequestPingPackage();
    }
}
