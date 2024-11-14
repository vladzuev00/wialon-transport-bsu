package by.bsu.wialontransport.protocol.wialon.handler;

import by.bsu.wialontransport.protocol.core.handler.packages.ignored.IgnoredPackageHandler;
import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonResponsePingPackage;
import org.springframework.stereotype.Component;

//TODO: test
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
