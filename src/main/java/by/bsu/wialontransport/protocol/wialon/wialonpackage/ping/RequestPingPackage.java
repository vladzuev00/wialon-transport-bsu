package by.bsu.wialontransport.protocol.wialon.wialonpackage.ping;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;

public final class RequestPingPackage implements Package {
    //should be #P# but mob application sends #P
    public static final String PREFIX = "#P";
}
