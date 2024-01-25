package by.bsu.wialontransport.protocol.wialon.model.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class WialonPackage implements Package {
    public static final String POSTFIX = "\r\n";

    public abstract String findPrefix();

    //TODO: test
    public final String findPostfix() {
        return POSTFIX;
    }
}
