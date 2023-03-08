package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.*;

@Value
public class ResponseBlackBoxPackage implements Package {
    public static final String PREFIX = "#AB#";

    int amountFixedMessages;
}
