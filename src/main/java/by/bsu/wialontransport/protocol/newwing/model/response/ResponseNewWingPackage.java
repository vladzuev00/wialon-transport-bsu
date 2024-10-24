package by.bsu.wialontransport.protocol.newwing.model.response;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class ResponseNewWingPackage implements Package {
    private final String value;
}
