package by.bsu.wialontransport.protocol.wialon.model.packages;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class WialonPackage {
    public static final String POSTFIX = "\r\n";

    public abstract String getPrefix();
}
