package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.*;

@Value
public class WialonResponseBlackBoxPackage implements WialonPackage {
    public static final String PREFIX = "#AB#";

    int amountFixedMessages;
}