package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage.PREFIX;

public final class WialonRequestPingPackageDecoder extends WialonPackageDecoder<WialonRequestPingPackage> {

    public WialonRequestPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestPingPackage decodeMessage(final String message) {
        return new WialonRequestPingPackage();
    }
}
