package by.vladzuev.locationreceiver.protocol.wialon.model.packages.ping;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;
import lombok.Value;

@Value
public class WialonRequestPingPackage implements WialonPackage {
    public static final String PREFIX = "#P#";

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
