package by.bsu.wialontransport.protocol.wialon.wialonpackage.message;

import lombok.Value;

@Value
public class RequestMessagePackage {
    public static final String PREFIX = "#M#";

    String message;
}
