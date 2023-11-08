package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.LoginResponseProvider;
import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import by.bsu.wialontransport.protocol.core.service.login.TrackerUnprotectedLoginService;

public abstract class UnprotectedLoginPackageHandler<
        PACKAGE extends LoginPackage,
        RESPONSE_PROVIDER extends LoginResponseProvider
        >
        extends LoginPackageHandler<PACKAGE, RESPONSE_PROVIDER, TrackerUnprotectedLoginService> {

    public UnprotectedLoginPackageHandler(final Class<PACKAGE> handledPackageType,
                                          final TrackerUnprotectedLoginService loginService,
                                          final RESPONSE_PROVIDER responseProvider) {
        super(handledPackageType, loginService, responseProvider);
    }

}
