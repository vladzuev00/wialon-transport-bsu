package by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;

@Getter
public abstract class ProtectedLoginResponseProvider extends LoginResponseProvider {
    private final Package wrongPasswordResponse;

    public ProtectedLoginResponseProvider(final Package successResponse,
                                          final Package noSuchImeiResponse,
                                          final Package wrongPasswordResponse) {
        super(successResponse, noSuchImeiResponse);
        this.wrongPasswordResponse = wrongPasswordResponse;
    }
}
