package by.vladzuev.locationreceiver.protocol.wialon.model.packages.login;

import by.vladzuev.locationreceiver.protocol.core.model.ProtectedLoginPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;
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
