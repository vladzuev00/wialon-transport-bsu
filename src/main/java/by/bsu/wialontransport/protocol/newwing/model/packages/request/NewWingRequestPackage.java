package by.bsu.wialontransport.protocol.newwing.model.packages.request;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class NewWingRequestPackage implements Package {
    private final int checksum;
}
