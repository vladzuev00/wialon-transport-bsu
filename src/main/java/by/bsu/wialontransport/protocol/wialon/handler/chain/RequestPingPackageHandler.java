package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class RequestPingPackageHandler extends PackageHandler {

    public RequestPingPackageHandler(PackageHandler nextHandler) {
        super(packageType, nextHandler);
    }

    @Override
    protected void handleIndependently(Package requestPackage, ChannelHandlerContext context) {

    }
}
