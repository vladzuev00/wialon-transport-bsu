package by.bsu.wialontransport.protocol.core.service.login.responseprovider;

import by.bsu.wialontransport.protocol.core.model.packages.Package;

public final class UnprotectedLoginResponseProvider extends LoginResponseProvider {

    public UnprotectedLoginResponseProvider(final Package successResponse, final Package noSuchImeiResponse) {
        super(successResponse, noSuchImeiResponse);
    }

}
