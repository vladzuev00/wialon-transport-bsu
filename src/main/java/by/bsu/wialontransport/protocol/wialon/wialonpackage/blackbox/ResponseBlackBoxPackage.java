package by.bsu.wialontransport.protocol.wialon.wialonpackage.blackbox;

import lombok.*;

@Value
public class ResponseBlackBoxPackage {
    public static final String PREFIX = "#AB#";

    int amountFixedMessages;
}
