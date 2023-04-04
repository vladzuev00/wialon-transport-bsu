package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.handler.chain.data.RequestDataPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.RequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.ResponsePingPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class RequestPingPackageHandler extends PackageHandler {

    public RequestPingPackageHandler(final RequestDataPackageHandler nextHandler) {
        super(RequestPingPackage.class, nextHandler);
    }

    @Override
    protected void handleIndependently(final Package requestPackage, final ChannelHandlerContext context) {
        final ResponsePingPackage responsePingPackage = new ResponsePingPackage();
        context.writeAndFlush(responsePingPackage);
    }
}
