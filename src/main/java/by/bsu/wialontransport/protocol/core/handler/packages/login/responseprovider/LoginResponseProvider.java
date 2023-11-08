package by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class LoginResponseProvider {
    private final Package successResponse;
    private final Package noSuchImeiResponse;
}
