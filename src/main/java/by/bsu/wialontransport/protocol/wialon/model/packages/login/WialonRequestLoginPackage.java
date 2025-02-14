package by.bsu.wialontransport.protocol.wialon.model.packages.login;

import by.bsu.wialontransport.protocol.core.model.ProtectedLoginPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.Value;

@Value
public class WialonRequestLoginPackage implements WialonPackage, ProtectedLoginPackage {
    public static final String PREFIX = "#L#";

    String imei;
    String password;

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
