package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage.PREFIX;

public final class WialonRequestPingPackageDecoder extends WialonPackageDecoder<WialonRequestPingPackage> {

    public WialonRequestPingPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestPingPackage decodeMessage(final String message) {
        return new WialonRequestPingPackage();
    }
}