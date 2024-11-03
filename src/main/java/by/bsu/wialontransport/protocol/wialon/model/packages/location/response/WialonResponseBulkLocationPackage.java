package by.bsu.wialontransport.protocol.wialon.model.packages.location.response;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonResponseBulkLocationPackage extends WialonPackage {
    public static final String PREFIX = "#AB#";

    private final int fixedLocationCount;

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
