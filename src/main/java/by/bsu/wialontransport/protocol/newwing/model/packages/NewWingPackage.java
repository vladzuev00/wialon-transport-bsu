package by.bsu.wialontransport.protocol.newwing.model.packages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class NewWingPackage {
    private final int checksum;
}
