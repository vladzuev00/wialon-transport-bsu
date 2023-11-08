package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.ProtectedLoginResponseProvider;
import by.bsu.wialontransport.protocol.core.model.packages.ProtectedLoginPackage;
import by.bsu.wialontransport.protocol.core.service.login.TrackerProtectedLoginService;

public abstract class ProtectedLoginPackageHandler<
        PACKAGE extends ProtectedLoginPackage,
        RESPONSE_PROVIDER extends ProtectedLoginResponseProvider
        >
        extends LoginPackageHandler<PACKAGE, RESPONSE_PROVIDER, TrackerProtectedLoginService> {

    public ProtectedLoginPackageHandler(final Class<PACKAGE> handledPackageType,
                                        final TrackerProtectedLoginService loginService,
                                        final RESPONSE_PROVIDER responseProvider) {
        super(handledPackageType, loginService, responseProvider);
    }
}
