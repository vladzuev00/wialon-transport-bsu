package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage.PREFIX;

@Component
public final class WialonRequestPingPackageDecoder extends WialonPackageDecoder {

    public WialonRequestPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestPingPackage decodeMessage(final String message) {
        return new WialonRequestPingPackage();
    }

    @Override
    protected int getLength(String s) {
        return 0;
    }
}
