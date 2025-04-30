package by.vladzuev.locationreceiver.protocol.wialon.model.location.response;

import by.vladzuev.locationreceiver.protocol.wialon.model.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class WialonResponseBulkLocationPackage implements WialonPackage {
    public static final String PREFIX = "#AB#";

    private final int fixedLocationCount;

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
