package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.core.service.login.TEMPAuthorizationTrackerService;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class RequestLoginPackageHandler extends PackageHandler {
    private final TEMPAuthorizationTrackerService authorizationTrackerService;

    public RequestLoginPackageHandler(final RequestPingPackageHandler nextHandler,
                                      final TEMPAuthorizationTrackerService authorizationTrackerService) {
        super(WialonRequestLoginPackage.class, nextHandler);
        this.authorizationTrackerService = authorizationTrackerService;
    }

    @Override
    protected void handleIndependently(final WialonPackage requestPackage, final ChannelHandlerContext context) {
        final WialonRequestLoginPackage requestLoginPackage = (WialonRequestLoginPackage) requestPackage;
        this.authorizationTrackerService.authorize(requestLoginPackage, context);
    }
}
