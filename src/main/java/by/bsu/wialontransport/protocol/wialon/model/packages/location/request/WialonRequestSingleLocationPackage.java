package by.bsu.wialontransport.protocol.wialon.model.packages.location.request;

import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;

import static java.util.Collections.singletonList;

public final class WialonRequestSingleLocationPackage extends WialonRequestLocationPackage {
    public static final String PREFIX = "#D#";

    public WialonRequestSingleLocationPackage(final WialonLocation location) {
        super(singletonList(location));
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
