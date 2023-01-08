package by.bsu.wialontransport.protocol.wialonpackage.blackbox;

import lombok.*;

@Value
public class ResponseBlackBoxPackage {
    public static final String PREFIX = "#AB#";

    int amountFixedMessages;
}
