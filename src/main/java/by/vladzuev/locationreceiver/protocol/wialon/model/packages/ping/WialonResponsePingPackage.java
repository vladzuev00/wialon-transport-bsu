package by.vladzuev.locationreceiver.protocol.wialon.model.packages.ping;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;

public final class WialonResponsePingPackage implements WialonPackage {
    public static final String PREFIX = "#AP#";

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
