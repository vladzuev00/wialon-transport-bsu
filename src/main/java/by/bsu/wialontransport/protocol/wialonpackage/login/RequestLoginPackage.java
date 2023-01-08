package by.bsu.wialontransport.protocol.wialonpackage.login;

import lombok.Value;

@Value
public class RequestLoginPackage {
    public static final String PREFIX = "#L#";

    String imei;
    String password;
}
