package by.bsu.wialontransport.protocol.newwing.handler.packages.login;

import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.LoginResponseProvider;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.FailureResponseNewWingPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.SuccessResponseNewWingPackage;

public final class NewWingLoginResponseProvider extends LoginResponseProvider {

    public NewWingLoginResponseProvider(final SuccessResponseNewWingPackage successResponse,
                                        final FailureResponseNewWingPackage noSuchImeiResponse) {
        super(successResponse, noSuchImeiResponse);
    }

}
