package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonResponsePingPackage;
import io.netty.channel.ChannelHandlerContext;

public final class WialonRequestPingPackageHandler extends PackageHandler<WialonRequestPingPackage> {

    public WialonRequestPingPackageHandler() {
        super(WialonRequestPingPackage.class);
    }

    @Override
    protected Package handleInternal(final WialonRequestPingPackage request, final ChannelHandlerContext context) {
        return new WialonResponsePingPackage();
    }
}