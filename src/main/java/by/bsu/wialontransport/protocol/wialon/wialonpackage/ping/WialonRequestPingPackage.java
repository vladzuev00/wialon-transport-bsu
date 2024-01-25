package by.bsu.wialontransport.protocol.wialon.wialonpackage.ping;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public final class WialonRequestPingPackage extends WialonPackage {
    //#P# is correct but mob application sends #P
    private static final String PREFIX = "#P#";

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
