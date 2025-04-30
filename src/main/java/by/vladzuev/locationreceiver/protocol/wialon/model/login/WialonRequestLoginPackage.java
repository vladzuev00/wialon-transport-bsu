package by.vladzuev.locationreceiver.protocol.wialon.model.login;

import by.vladzuev.locationreceiver.protocol.core.model.ProtectedLoginPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.WialonPackage;
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
