package by.bsu.wialontransport.protocol.wialon.model.packages.data.request;

import by.bsu.wialontransport.protocol.wialon.model.WialonData;

import static java.util.Collections.singletonList;

public final class WialonRequestDataPackage extends AbstractWialonRequestDataPackage {
    public static final String PREFIX = "#D#";

    public WialonRequestDataPackage(final WialonData data) {
        super(singletonList(data));
    }

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
