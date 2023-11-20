package by.bsu.wialontransport.protocol.newwing.handler.packages.login;

import by.bsu.wialontransport.protocol.core.handler.packages.login.UnprotectedLoginPackageHandler;
import by.bsu.wialontransport.protocol.core.service.login.TrackerUnprotectedLoginService;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.LoginNewWingPackage;

public final class NewWingLoginPackageHandler
        extends UnprotectedLoginPackageHandler<LoginNewWingPackage, NewWingLoginResponseProvider> {

    public NewWingLoginPackageHandler(final TrackerUnprotectedLoginService loginService,
                                      final NewWingLoginResponseProvider responseProvider) {
        super(LoginNewWingPackage.class, loginService, responseProvider);
    }

}
