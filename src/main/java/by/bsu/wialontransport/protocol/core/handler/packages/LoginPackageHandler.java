package by.bsu.wialontransport.protocol.core.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import by.bsu.wialontransport.protocol.core.service.login.TrackerLoginService;
import io.netty.channel.ChannelHandlerContext;

public abstract class LoginPackageHandler<PACKAGE extends LoginPackage> extends PackageHandler<PACKAGE> {
    private final TrackerLoginService<PACKAGE> loginService;

    public LoginPackageHandler(final Class<PACKAGE> handledPackageType,
                               final TrackerLoginService<PACKAGE> loginService) {
        super(handledPackageType);
        this.loginService = loginService;
    }

    @Override
    protected final void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        this.loginService.login(requestPackage, context);
    }
}
