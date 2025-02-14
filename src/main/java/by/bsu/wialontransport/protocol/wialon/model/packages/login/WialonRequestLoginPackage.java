package by.bsu.wialontransport.protocol.wialon.model.packages.login;

import by.bsu.wialontransport.protocol.core.model.ProtectedLoginPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

public final class WialonRequestLoginPackage extends ProtectedLoginPackage implements WialonPackage {
    public static final String PREFIX = "#L#";

    public WialonRequestLoginPackage(final String imei, final String password) {
        super(imei, password);
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
