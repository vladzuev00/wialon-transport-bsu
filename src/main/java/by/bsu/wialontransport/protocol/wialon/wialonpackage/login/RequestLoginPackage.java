package by.bsu.wialontransport.protocol.wialon.wialonpackage.login;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.Value;

@Value
public class RequestLoginPackage implements Package {
    public static final String PREFIX = "#L#";

    String imei;
    String password;
}
