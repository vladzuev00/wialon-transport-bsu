package by.vladzuev.locationreceiver.protocol.wialon.model.location.request;

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
