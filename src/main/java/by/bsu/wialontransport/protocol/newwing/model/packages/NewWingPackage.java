package by.bsu.wialontransport.protocol.newwing.model.packages;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class NewWingPackage implements Package {
    private final int checksum;
}
