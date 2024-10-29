package by.bsu.wialontransport.protocol.wialon.model.packages.location.request;

import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class WialonRequestLocationPackage extends WialonPackage {
    private final List<WialonLocation> locations;
}
