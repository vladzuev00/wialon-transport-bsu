package by.vladzuev.locationreceiver.protocol.wialon.handler;

import by.vladzuev.locationreceiver.protocol.core.handler.packages.ignored.IgnoredPackageHandler;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.ping.WialonResponsePingPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestPingPackageHandler extends IgnoredPackageHandler<WialonRequestPingPackage> {

    public WialonRequestPingPackageHandler() {
        super(WialonRequestPingPackage.class);
    }

    @Override
    protected WialonResponsePingPackage createResponse() {
        return new WialonResponsePingPackage();
    }
}
