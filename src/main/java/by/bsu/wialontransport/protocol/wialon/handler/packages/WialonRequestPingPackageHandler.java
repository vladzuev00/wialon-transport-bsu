package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonResponsePingPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestPingPackageHandler extends PackageHandler<WialonRequestPingPackage> {

    public WialonRequestPingPackageHandler() {
        super(WialonRequestPingPackage.class);
    }

    @Override
    protected Package handleInternal(final WialonRequestPingPackage request, final ChannelHandlerContext context) {
        return new WialonResponsePingPackage();
    }
}
