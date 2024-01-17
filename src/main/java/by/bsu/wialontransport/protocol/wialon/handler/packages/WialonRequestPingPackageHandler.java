package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;
import io.netty.channel.ChannelHandlerContext;

public final class WialonRequestPingPackageHandler extends PackageHandler<WialonRequestPingPackage> {

    public WialonRequestPingPackageHandler() {
        super(WialonRequestPingPackage.class);
    }

    @Override
    protected void handleConcretePackage(final WialonRequestPingPackage requestPackage,
                                         final ChannelHandlerContext context) {
        
    }
}
