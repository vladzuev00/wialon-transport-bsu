package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import by.bsu.wialontransport.protocol.core.service.login.TrackerLoginService;
import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.LoginResponseProvider;
import io.netty.channel.ChannelHandlerContext;

public abstract class LoginPackageHandler<
        PACKAGE extends LoginPackage,
        RESPONSE_PROVIDER extends LoginResponseProvider,
        LOGIN_SERVICE extends TrackerLoginService<? super PACKAGE, ? super RESPONSE_PROVIDER>
        >
        extends PackageHandler<PACKAGE> {
    private final LOGIN_SERVICE loginService;
    private final RESPONSE_PROVIDER responseProvider;

    public LoginPackageHandler(final Class<PACKAGE> handledPackageType,
                               final LOGIN_SERVICE loginService,
                               final RESPONSE_PROVIDER responseProvider) {
        super(handledPackageType);
        this.loginService = loginService;
        this.responseProvider = responseProvider;
    }

    @Override
    protected final void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        this.loginService.login(requestPackage, this.responseProvider, context);
    }
}
