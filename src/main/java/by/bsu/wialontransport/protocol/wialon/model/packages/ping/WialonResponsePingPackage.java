package by.bsu.wialontransport.protocol.wialon.model.packages.ping;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

public final class WialonResponsePingPackage extends WialonPackage {
    public static final String PREFIX = "#AP#";

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
