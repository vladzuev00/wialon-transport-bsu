package by.bsu.wialontransport.protocol.wialon.wialonpackage.login;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.Value;

@Value
public class RequestLoginPackage implements WialonPackage {
    public static final String PREFIX = "#L#";

    String imei;
    String password;
}
