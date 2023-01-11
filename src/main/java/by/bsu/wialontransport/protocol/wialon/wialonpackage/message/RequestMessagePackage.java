package by.bsu.wialontransport.protocol.wialon.wialonpackage.message;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.Value;

@Value
public class RequestMessagePackage implements Package {
    public static final String PREFIX = "#M#";

    String message;
}
