package by.bsu.wialontransport.protocol.wialon.wialonpackage.ping;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public final class WialonResponsePingPackage extends WialonPackage {
    public static final String PREFIX = "#AP#";

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
