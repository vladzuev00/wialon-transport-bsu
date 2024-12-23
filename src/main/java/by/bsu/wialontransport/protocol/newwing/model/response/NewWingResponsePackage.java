package by.bsu.wialontransport.protocol.newwing.model.response;

import by.bsu.wialontransport.protocol.core.model.Package;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class NewWingResponsePackage implements Package {
    private final String value;
}
