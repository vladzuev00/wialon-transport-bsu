package by.bsu.wialontransport.protocol.wialon.wialonpackage;

import by.bsu.wialontransport.protocol.core.model.packages.Package;

public abstract class WialonPackage implements Package {
    private static final String POSTFIX = "\r\n";

    public abstract String getPrefix();

    //TODO: test
    public final String getPostfix() {
        return POSTFIX;
    }
}
