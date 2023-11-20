package by.bsu.wialontransport.protocol.newwing.model.packages.response;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class ResponseNewWingPackage implements Package {
    private final String value;
}
