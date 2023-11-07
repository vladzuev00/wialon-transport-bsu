package by.bsu.wialontransport.protocol.core.handler.packages;

import by.bsu.wialontransport.protocol.core.service.authorization.TEMPAuthorizationTrackerService;
import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public abstract class LoginPackageHandler<PACKAGE extends LoginPackage> extends PackageHandler<PACKAGE> {
    private final TEMPAuthorizationTrackerService authorizationTrackerService;

    public LoginPackageHandler(final Class<PACKAGE> handledPackageType,
                               final TEMPAuthorizationTrackerService authorizationTrackerService) {
        super(handledPackageType);
        this.authorizationTrackerService = authorizationTrackerService;
    }

    //TODO: подумать как обработать исключительные ситуации
    @Override
    protected final void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        final String trackerImei = requestPackage.getImei();
        final Optional<String> optionalPassword = requestPackage.findPassword();
        optionalPassword.ifPresentOrElse(
                password -> this.authorizationTrackerService.authorize(trackerImei, password, context),
                () -> this.authorizationTrackerService.authorize(trackerImei, context)
        );
    }
}
