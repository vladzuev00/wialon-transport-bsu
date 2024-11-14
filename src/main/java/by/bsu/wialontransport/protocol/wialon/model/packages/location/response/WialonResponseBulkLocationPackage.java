package by.bsu.wialontransport.protocol.wialon.model.packages.location.response;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class WialonResponseBulkLocationPackage implements WialonPackage {
    public static final String PREFIX = "#AB#";

    private final int fixedLocationCount;

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
