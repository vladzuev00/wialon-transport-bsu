package by.bsu.wialontransport.protocol.wialon.model.packages.ping;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

public final class WialonRequestPingPackage extends WialonPackage {
    //#P# is correct but mob application sends #P
    public static final String PREFIX = "#P#";

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
