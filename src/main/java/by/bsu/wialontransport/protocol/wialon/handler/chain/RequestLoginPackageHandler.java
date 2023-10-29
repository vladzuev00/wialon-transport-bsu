package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.core.service.authorization.AuthorizationTrackerService;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class RequestLoginPackageHandler extends PackageHandler {
    private final AuthorizationTrackerService authorizationTrackerService;

    public RequestLoginPackageHandler(final RequestPingPackageHandler nextHandler,
                                      final AuthorizationTrackerService authorizationTrackerService) {
        super(RequestLoginPackage.class, nextHandler);
        this.authorizationTrackerService = authorizationTrackerService;
    }

    @Override
    protected void handleIndependently(final WialonPackage requestPackage, final ChannelHandlerContext context) {
        final RequestLoginPackage requestLoginPackage = (RequestLoginPackage) requestPackage;
        this.authorizationTrackerService.authorize(requestLoginPackage, context);
    }
}
