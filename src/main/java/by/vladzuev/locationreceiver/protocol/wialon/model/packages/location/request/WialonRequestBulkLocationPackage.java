package by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.request;

import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation;

import java.util.List;

public final class WialonRequestBulkLocationPackage extends WialonRequestLocationPackage {
    public static final String PREFIX = "#B#";

    public WialonRequestBulkLocationPackage(final List<WialonLocation> locations) {
        super(locations);
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
